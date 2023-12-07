;; cards.clj --- Scratchcards
(ns cards
  (:require [clojure.string]))

(def input "32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483")

(def rank-map {:A 14 :K 13 :Q 12 :J 1 :T 10 :9 9 :8 8 :7 7 :6 6 :5 5 :4 4 :3 3 :2 2})

(def type-map {:five-of-a-kind 9 :four-of-a-kind 8 :full-house 7 :three-of-a-kind 6 :two-pair 5 :one-pair 4 :high-card 3})

(defn string-to-character-frequencies [s]
  (frequencies s))

(defn consolidate-jokers [frequencies]
  (let [joker-count (get frequencies \J 0)]
    (if (or (zero? joker-count) (= joker-count 5)) (vals frequencies)
        (let [freqs-without-joker (dissoc frequencies \J)
              rank-to-be-replaced (key (apply max-key val freqs-without-joker))]
          (vals (assoc freqs-without-joker rank-to-be-replaced
                       (+ (get freqs-without-joker rank-to-be-replaced 0)
                          joker-count)))))))
    
(defn find-vals [hand] 
  (consolidate-jokers (string-to-character-frequencies hand)))

(defn get-hand-type [hand]
    (let [ values (find-vals hand)
            sorted-values (sort values)
            sorted-values-reversed (reverse sorted-values)] 
      (cond
        (= sorted-values-reversed [5]) :five-of-a-kind
        (= sorted-values-reversed [4 1]) :four-of-a-kind
        (= sorted-values-reversed [3 2]) :full-house
        (= sorted-values-reversed [3 1 1]) :three-of-a-kind 
        (= sorted-values-reversed [2 2 1]) :two-pair 
        (= sorted-values-reversed [2 1 1 1]) :one-pair
        :else :high-card)))

(defn compare-ranks [rank1 rank2]
  (compare (rank-map (keyword (str rank1))) (rank-map (keyword (str rank2)))))

(defn non-zero? [x]
  (not (= x 0)))

(defn compare-hands [hand1 hand2]
  (let [res (first (filter #(not= % 0) (map compare-ranks hand1 hand2)))]
    (cond
      (nil? res) nil
      (> res 0) 1
      (< res 0) -1
      :else nil)))

(defn sort-hands [h1 h2]
  (let [hand1 (h1 :hand)
        hand2 (h2 :hand)
        hand1-type (type-map (get-hand-type hand1))
        hand2-type (type-map (get-hand-type hand2))] 
    (cond
      (> hand1-type hand2-type) 1
      (< hand1-type hand2-type) -1
      :else (compare-hands hand1 hand2))))

;; Parsing logic
(defn parse-line [line]
    (->> (clojure.string/split line #"\s")
         (filter #(not (clojure.string/blank? %)))))

(defn parse-hand [line]
  (let [vals (parse-line line)
        hand (first vals)
        bid (Integer. (second vals))] 
    {:hand hand :bid bid}))

(defn parse-input [input]
  (map parse-hand (clojure.string/split-lines input)))

(defn accumulate-bids [acc bid] 
    (assoc acc bid (+ (acc bid) 1)))

(defn engine [input]
    (let [hands (parse-input input)
            sorted-hands (sort sort-hands hands)]
      (reduce + (map-indexed (fn [idx val] (* (+ idx 1) val)) (map #(:bid %) sorted-hands)))))

(defn solve [path]
  (engine (slurp path)))