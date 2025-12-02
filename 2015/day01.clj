(ns aoc.2015.day01
  (:require [clojure.string :as string]))

(defn count-floor
  "Given a string of parentheses, return the floor number."
    [input]
    (->> (clojure.string/split input #"")
         (filter #(not (clojure.string/blank? %))) 
         (reduce (fn [acc x] (if (= x "(") (inc acc) (dec acc))) 0)))

         
(defn solve1
  "Given a string of parentheses, return the floor number."
  [s]
  (count-floor s))

(defn solve2
    "Given a string of parentheses, return the floor number."
    [s]
    (count-floor s))

(defn main
  "Given a string of parentheses, return the floor number."
  [& args]
  (let [input (slurp (first args))]
    (println (solve1 input))
    (println (solve2 input))))