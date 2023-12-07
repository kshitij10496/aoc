(ns aoc.2023.day07
  (:require [clojure.string]))

;; Map to store the strength of each card.
;; The values are arbitrary, but the order is important.
(def card-to-strength {:A 14 
                       :K 13 
                       :Q 12 
                       :J 11 
                       :T 10 
                       :9 9 
                       :8 8 
                       :7 7 
                       :6 6
                       :5 5
                       :4 4
                       :3 3
                       :2 2})

;; Map to store the strength of each card, including the joker.
;; The values are arbitrary, but the order is important.
(def card-to-strength-with-joker (assoc card-to-strength :J 1))

;; Map to store the strength of each hand type.
;; The values are arbitrary, but the order is important.
(def hand-type-to-strength {:five-of-a-kind 9
                            :four-of-a-kind 8
                            :full-house 7
                            :three-of-a-kind 6
                            :two-pair 5
                            :one-pair 4
                            :high-card 3})

;; ==================
;; Hand type logic
;; ==================

(defn build-char-map-without-jokers [hand] (frequencies hand))

(defn consolidate-jokers [frequencies]
  (let [joker-count (get frequencies \J 0)]
    (if (or (zero? joker-count) (= joker-count 5)) frequencies
        (let [freqs-without-joker (dissoc frequencies \J)
              rank-to-be-replaced (key (apply max-key val freqs-without-joker))]
          (assoc freqs-without-joker rank-to-be-replaced
                       (+ (get freqs-without-joker rank-to-be-replaced 0)
                          joker-count))))))


(defn build-char-map-with-jokers [hand]
  (consolidate-jokers (build-char-map-without-jokers hand)))

(defn build-hand-config [builder hand] 
  (reverse (sort (vals (builder hand)))))

(defn get-builder [joker]
  (if joker
    build-char-map-with-jokers
    build-char-map-without-jokers))

(defn get-hand-type [hand config-builder]
  (let [config (build-hand-config config-builder hand)] 
    (cond
      (= config [5]) :five-of-a-kind
      (= config [4 1]) :four-of-a-kind
      (= config [3 2]) :full-house
      (= config [3 1 1]) :three-of-a-kind
      (= config [2 2 1]) :two-pair
      (= config [2 1 1 1]) :one-pair
      :else :high-card)))

;; ==================
;; Hand comparison logic
;; ==================

(defn compare-cards-char-by-char
  "Returns 1 if c1 is stronger, -1 if c2 is stronger, and 0 if they are equal or the cards are incomparable.
   
   It implements java.util.Comparator.compare."
  [c1 c2]
  (compare (card-to-strength (keyword (str c1))) 
           (card-to-strength (keyword (str c2)))))

(defn compare-cards-char-by-char-with-joker
  "Returns 1 if c1 is stronger, -1 if c2 is stronger, and 0 if they are equal or the cards are incomparable.
   
   It implements java.util.Comparator.compare."
  [c1 c2]
  (compare (card-to-strength-with-joker (keyword (str c1)))
           (card-to-strength-with-joker (keyword (str c2)))))


(defn compare-hands-second-order 
  "Returns 1 if hand1 is stronger, -1 if hand2 is stronger, and 0 if they are equal or the hands are incomparable.
   It implements java.util.Comparator.compare."
  [h1 h2] 
  (let [res (first (filter #(not= % 0) (map compare-cards-char-by-char 
                                            h1 
                                            h2)))]
    (cond
      (nil? res) 0
      (> res 0) 1
      (< res 0) -1
      :else 0)))

(defn compare-hands-second-order-with-joker
  "Returns 1 if hand1 is stronger, -1 if hand2 is stronger, and 0 if they are equal or the hands are incomparable.
   It implements java.util.Comparator.compare."
  [h1 h2]
  (let [res (first (filter #(not= % 0) (map compare-cards-char-by-char-with-joker
                                            h1
                                            h2)))]
    (cond
      (nil? res) 0
      (> res 0) 1
      (< res 0) -1
      :else 0)))

(defn compare-hands-first-order
  "Returns 1 if hand1 is stronger, -1 if hand2 is stronger, and 0 if they are equal or the hands are incomparable.
   
   It implements java.util.Comparator.compare."
  [h1 h2]
  (let [h1-strength (hand-type-to-strength h1)
        h2-strength (hand-type-to-strength h2)] 
    (compare h1-strength 
             h2-strength)))

(defn compare-hands 
  "Returns 1 if hand1 is stronger, -1 if hand2 is stronger, and 0 if they are equal or the hands are incomparable.
   
   First-ordering rule: hand type strength.
   Second-ordering rule: compare hands sequentially.
   
   It implements java.util.Comparator.compare."
  [h1 h2]
  (let [res-first-order (compare-hands-first-order (:type h1) (:type h2))]
    (if (zero? res-first-order)
      (compare-hands-second-order (:hand h1) (:hand h2))
      res-first-order)))

(defn compare-hands-with-joker
  "Returns 1 if hand1 is stronger, -1 if hand2 is stronger, and 0 if they are equal or the hands are incomparable.
   
   First-ordering rule: hand type strength.
   Second-ordering rule: compare hands sequentially.
   
   It implements java.util.Comparator.compare."
  [h1 h2]
  (let [res-first-order (compare-hands-first-order (:type h1) (:type h2))]
    (if (zero? res-first-order)
      (compare-hands-second-order-with-joker (:hand h1) (:hand h2))
      res-first-order)))

(defn rank-hands 
  "Ranks the hands from strongest to weakest." 
  [hands & joker]
  (println joker)
  (if joker
    (sort compare-hands-with-joker hands)
    (sort compare-hands hands)))


(defn bid-to-winnings 
  "Computes the winnings for a ranked list of bids." 
  [ranked-bids] 
  (reduce + (map-indexed (fn [idx val] (* (+ idx 1) val)) ranked-bids)))

(defn winnings
  "Computes the winnings for a ranked list of hands."
  [ranked-hands]
  (bid-to-winnings (map #(:bid %) ranked-hands)))


;; ==================
;; Parsing logic
;; ==================
(defn parse-hand 
  "Parses a line into a hand and a bid."
  [line builder]
  (if (clojure.string/blank? line) nil
      (let [vals (clojure.string/split line #"\s")
            hand (first vals)
            bid (Integer. (second vals))]
        {:hand hand :bid bid :type (get-hand-type hand builder)})))

(defn parse-input 
  "Parses the input into a list of hands."
  [input]
  (map #(parse-hand % (get-builder false)) (clojure.string/split-lines input)))

(defn parse-input-with-joker
  "Parses the input into a list of hands."
  [input]
  (map #(parse-hand % (get-builder true)) (clojure.string/split-lines input)))

;; ==================
;; Main
;; ==================

(defn solve1 [input]
  (let [hands (parse-input input)]
    (winnings (rank-hands hands))))

(defn solve2 [input]
  (let [hands (parse-input-with-joker input)]
    (winnings (rank-hands hands true))))

(defn main
    "Main entry point."
    [path]
    (if (empty? path)
        (println "Usage: day07.clj <input-file>") 
        (do
          (println "Part 1:" (solve1 (slurp path)))
          (println "Part 2:" (solve2 (slurp path))))))
