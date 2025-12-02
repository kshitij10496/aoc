(ns aoc.2023.day14
   (:require [clojure.string :as clojure.string]))

(def foo "O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....")

(defn count-rocks
  [row] 
  (get (frequencies row) \O 0))

(defn count-rocks-row
  [row]
  ;; (println row (count (filter #(= % "O") row)))
  (count (filter #(= % "O") row))
  )

(defn count-cube-rocks
  [row]
  (get (frequencies row) \# 0))

(defn count-space
  [row]
  (get (frequencies row) \. 0))

(defn tilt-segment
  [segment]
  (let [count-of-rocks (count-rocks segment)
        count-of-cube-rocks (count-cube-rocks segment)
        count-of-spaces (count-space segment)
        rocks (clojure.string/join "" (repeat count-of-rocks "O"))
        spaces (clojure.string/join "" (repeat count-of-spaces "."))]
    (if (> count-of-cube-rocks 0) segment
     (clojure.string/join "" [rocks spaces]))))

(defn segments
  [row]
  (->> (partition-by #(= "#" %) row)
        (map #(clojure.string/join "" %))
        (map tilt-segment)
        (map #(clojure.string/split % #""))
        (reduce concat)))

(defn counter
  [rows]
  (->> (map-indexed (fn [i row] (* (+ i 1) (count-rocks-row row))) rows) 
       (reduce +)))

(defn transpose-space
  [space]
  (apply mapv vector space))

(defn build-space
  [input]
  (->> input
       (filter #(not (clojure.string/blank? %)))
       (map #(clojure.string/split % #""))))

(defn build-row
  [row]
  (clojure.string/join "" row))

(defn solve1
  [input]
  (let [space (build-space (clojure.string/split-lines input))]
    (counter (reverse (transpose-space (->> (transpose-space space) 
         (map #(segments %))
        ;;  (println)
         ))))))

;; (defn bar 
;;   [input]
;;   (reflector/transpose-space (reflector/build-space (reflector/solve1 input)))
;;   )

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))))
    ;; (println "Solution 2:" (solve2 input))))