(ns aoc.2023.day15
  (:require [clojure.string :as clojure.string]))

;; Create the store of boxes.
;; The index of each box is their ID.
;; The element at an index is an ordered list of lens.
(def boxes (vec (repeat 256 [])))

(defn make-lens
  [label & [power]]
  {:label label :power power})

(defn lens-label [l] (:label l))

(defn lens-power [l] (:power l))

(defn parse-lens
  [s]
  (let [matcher #"(.*)=(\d)"
        m (re-matches matcher s)]
    (make-lens (get m 1) (get m 2))))


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

(defn build-instructions
  [input]
  (->> (clojure.string/split input #",")
       (filter #(not (clojure.string/blank? %)))))
  
(defn focusing-power
  [box slot focal-length]
  (* box slot focal-length))


(defn solve1
  [input]
  (->> (build-instructions input)
        (map #(hash-str %))
        (reduce +)))

(defn box-power
  [box-id lenses]
  (->> (map-indexed (fn [idx lens] (focusing-power box-id (+ idx 1) (lens-power lens))) lenses) 
       (reduce +)))


(defn updated-lens
  [lenses l]
  (vec (for [bl lenses]
    (if (= (lens-label bl) (lens-label l)) (make-lens (lens-label l) (lens-power l))
        bl))))

(defn add-lens
  [lenses l]
  (vec (conj lenses l)))

(defn contains-lens?
  [lenses l]
  (some #(= (lens-label %) (lens-label l)) lenses))

(defn upsert-lens
  [lenses l]
  (if (contains-lens? lenses l)
    (updated-lens lenses l)
    (add-lens lenses l)))

(defn remove-lens
  [lenses l]
  (vec (filter #(not= (lens-label %) (lens-label l)) lenses)))

(defn operate-box
  [box lens removal?]
  (if removal? 
    (remove-lens box lens) 
    (upsert-lens box lens)))

(defn parse-instruction
  [s]
  (let [removal? (clojure.string/ends-with? s "-")
        matcher-upsert #"(\w+)=(\d+)"
        matcher-removal #"(\w+)-"
        mu (re-matches matcher-upsert s)
        mr (re-matches matcher-removal s)
        label (if removal? (nth mr 1) (nth mu 1))
        power (if removal? 0 (Integer. (nth mu 2)))]
    {:label label :removal? removal? :power power}))

(defn apply-instruction
  [instruction boxes]
  (let [op (parse-instruction instruction)
        box-id (hash-str (:label op))
        box (get boxes box-id)
        res (operate-box box (make-lens (:label op) (:power op)) (:removal? op))]
    (assoc boxes box-id res)
    ))

(defn apply-instructions 
  [instructions]
  (let [boxes boxes]
     (loop [instructions instructions
            res boxes]
       (if (or (nil? instructions) (empty? instructions))
         res
         (recur (rest instructions) (apply-instruction (first instructions) res))))))

(defn solve2
  [input]
  (let [instructions (build-instructions input)] 
    (->> (apply-instructions instructions)
         (map-indexed (fn [idx box] (box-power (+ idx 1) box)))
         (reduce +))))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))
    (println "Solution 2:" (solve2 input))))