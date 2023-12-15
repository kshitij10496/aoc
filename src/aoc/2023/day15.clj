(ns aoc.2023.day15
  (:require [clojure.string :as clojure.string]))

(defn make-lens
  [label power]
  {:label label :power power})

(defn lens-label [l] (:label l))

(defn lens-power [l] (:power l))

(defn parse-lens
  [s]
  (let [matcher #"(.*)=(\d)"
        m (re-matches matcher s)]
    (make-lens (get m 1) (get m 2))))

(defn eq-lens
  [l1 l2]
  (and (= (lens-label l1) (lens-label l2))))

(defn make-box
  [id lenses]
  {:id id :lens (vector lenses)})

(defn box-id [box] (:id box))

(defn box-lens [box] (:lens box))

(defn eq-box 
  [b1 b2]
  (and (= (box-id b1) (box-id b2)) (= (count (box-lens b1)) (count (box-lens b2)))))

(defn remove-lens
  [box ll]
  (make-box (box-id box) (filter #(not= (lens-label %) ll) (box-lens box))))

(defn contains-lens?
  [box l]
  (some #(= (lens-label %) (lens-label l)) (box-lens box)))

(defn updated-lens
  [lens l]
  (for [bl lens]
    (if (= (lens-label bl) (lens-label l)) (make-lens (lens-label l) (lens-power l))
        (make-lens (lens-label bl) (lens-power bl)))))

(defn add-lens
  [box l]
  (if (contains-lens? box l)
    (make-box (box-id box) (updated-lens (box-lens box) l))
    (make-box (box-id box) (conj (box-lens box) l))))

(defn hash-char
  "Returns the hash value of a single character.
   An optional initial value can be provided as well.
   "
  [c & [v]]
  (let [initial-val (or v 0)
        ascii-val (int c)]
    (mod (* (+ initial-val ascii-val) 17) 256)))

(defn hash-str 
  [s]
  (let [ss (seq (clojure.string/trim s))
        v 0] 
    (loop [ss ss 
           res v] 
      (if (empty? ss) 
        res 
        (recur (rest ss) (hash-char (first ss) res))))))

(defn hash-sequence
  [instructions]
  (->> (clojure.string/split instructions #",")
       (filter #(not (clojure.string/blank? %)))
       (map #(hash-str %))
       (reduce +)))

(defn focusing-power
  [box slot focal-length]
  (* (+ box 1) (+ slot 1) focal-length))

(defn solve1
  [input]
  (hash-sequence input))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))))