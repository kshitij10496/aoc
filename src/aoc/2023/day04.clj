(ns aoc.2023.day04
  (:require [clojure.string])
  (:require [clojure.set]))

;; Parsing the input
(defn parse-card [line]
    ;; Using regex to find all the numbers in a given line.
    ;; The regex is \d+ which means one or more digits.
    ;; It returns a collection of maps of cards with their ID, list of winning numbers and list of numbers we have drawn.
    (let [matcher #"Card(\s*)(\d+): (.*)\|(.*)"
          m (re-matches matcher line)
          id (Integer. (get m 2))
          winners (filter #(not (clojure.string/blank? %)) (clojure.string/split (get m 3) #" "))
          draws (filter #(not (clojure.string/blank? %)) (clojure.string/split (get m 4) #" "))] 
      {:id id
       :winners (map #(Integer. %) winners)
       :draws (map #(Integer. %) draws)}))

(defn build-deck [text]
  ;; Returns a deck of cards from a given text.
  ;; Splitting the text into lines, then mapping over the lines to build the sequence of cards. 
  (->> text
       (clojure.string/split-lines)
       (filter #(not (clojure.string/blank? %)))
       (map parse-card)))


(defn common-elements-count [vec1 vec2]
  ;; Returns the number of elements common to 2 given vectors.
  (->> (clojure.set/intersection (set vec1) (set vec2))
       count))

(defn num-winners [card]
  ;; Returns the number of winning draws in a card.
  (common-elements-count (:draws card) (:winners card)))

(defn points [card]
  ;; Returns the number of points scored for a given card.
  (let [count (num-winners card)]
    (if (= count 0)
      0
      (Math/pow 2 (- count 1)))))

(defn deck-points [deck]
  ;; Returns the sum of points for a given deck of cards.
  (reduce + (map points deck)))

(defn solve1 [path]
  ;; Solution for Part 1.
  (deck-points (build-deck (slurp path))))

;; Part 2
(defn create-vector [n]
  ;; Returns a unit vector of dimension 1xn.
  (vec (repeat n 1)))

(defn increment-range [vector n start-index end-index]
  ;; Returns a vector by incrementing a given range of values by n in the input vector v.
  (map-indexed (fn [i val]
                 (if (and (>= i start-index) (< i end-index))
                   (+ val n)
                   val))
               vector))

(defn c [card v]
  ;; Return a vector computed from the input vector based on a card's winners.
  (let [count (num-winners card)
        start (:id card)
        end (+ start count)
        idx (- start 1)
        n (nth v idx)] 
    (increment-range v n start end)))

(defn deck-all [deck]
  ;; Loop over each card in the deck to compute the final counter vector.
  (let [v (create-vector (count deck))]
    (loop [remaining-deck deck
           updated-counter v]
      (if (empty? remaining-deck)
        updated-counter
        (recur (rest remaining-deck)
               (c (first remaining-deck) updated-counter))))))

(defn num-cards [deck]
  ;; Returns the number of final cards for a given deck.
  (reduce + (deck-all deck)))

(defn solve2 [path]
  ;; Returns the solution to Part 2.
  (num-cards (build-deck (slurp path))))

(defn main
    "Main entry point."
    [path]
    (if (empty? path)
        (println "Usage: day04.clj <input-file>") 
        (do
        (println "Part 1:" (solve1 path))
        (println "Part 2:" (solve2 path)))))