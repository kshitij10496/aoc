(ns aoc.2023.day14
   (:require [clojure.string :as clojure.string]))

(defn count-rocks
  [row]
  (get (frequencies row) \0))

(defn count-space
  [row]
  (get (frequencies row) \.))

(defn tilt-segment
  [segment]
  (let [count-of-rocks (count-rocks segment)
        count-of-spaces (count-space segment)
        rocks (clojure.string/join "" (repeat count-of-rocks "0"))
        spaces (clojure.string/join "" (repeat count-of-spaces "."))]
    (clojure.string/join "" [rocks spaces])))

(defn segments
  [row]
  (clojure.string/join "#" (->> (clojure.string/split row #"#")
       (map tilt-segment))))

(defn counter
  [rows]
  (->> (map-indexed (fn [i row] (* (+ i 1) (count-rocks row))) rows)
       (reduce +)))

(defn transpose-space
  [space]
  (apply mapv vector space))

(defn solve1
  [input]
  (let [space (build-space input)]
    (->> (transpose-space space)
         (map segments)
         )
    )
  )

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))))
    ;; (println "Solution 2:" (solve2 input))))