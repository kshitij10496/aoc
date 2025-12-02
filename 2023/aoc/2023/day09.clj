(ns aoc.2023.day09
  (:require [clojure.string :as clojure.string]))

(defn diff
  "Given a sequence of numbers, return a sequence of the differences between the numbers."
  [s]
  (map - (rest s) s))

(defn zeros? 
  "Given a sequence of numbers, return true if all the numbers are zero." 
  [s] 
  (every? zero? s))

(defn predict
  "Given a sequence of numbers, return the next number in the sequence."
  [s]
  (let [d (diff s)]
    (if (zeros? d)
      (last s)
      (let [n (predict d)]
        (+ n (last s))))))

(defn extrapolate
  "Given a sequence of numbers, return the extrapolated backwards number in the sequence."
  [s]
  (let [d (diff s)]
    (if (zeros? d)
      (first s)
      (let [n (extrapolate d)]
        (- (first s) n)))))

(defn parse-sensor-data
  "Given a string of sensor data, return a sequence of numbers."
  [s]
  (->> (clojure.string/split s #" ")
       (filter #(not (clojure.string/blank? %))) 
       (map #(Integer/parseInt %))))

(defn parse-input
  "Given a string of sensor data, return a sequence of numbers."
    [input]
    (->> (clojure.string/split input #"\n")
         (filter #(not (clojure.string/blank? %))) 
         (map parse-sensor-data)))

(defn solve1
  "Given a list of sensor data, return the sum of the predicted numbers for every series."
  [s]
  (let [seqs (parse-input s)]
    (->> seqs
         (map predict)
         (reduce +))))

(defn solve2
    "Given a list of sensor data, return the sum of the predicted numbers for every series."
    [s]
    (let [seqs (parse-input s)]
        (->> seqs
             (map extrapolate)
             (reduce +))))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))
    (println "Solution 2:" (solve2 input))))