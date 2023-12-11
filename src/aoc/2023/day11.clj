(ns aoc.2023.day11
  (:require [clojure.math.combinatorics :as combo]
            [clojure.string :as clojure.string]))

(defn make-point
  "Creates a 2D point in space with the given x and y co-ordinates"
  [x y] (vector x y))

(defn x-coordinate
  "Returns the x co-ordinate of a 2D point."
  [p] (first p))

(defn y-coordinate
  "Returns the y co-ordinate of a 2D point."
  [p] (last p))

(defn distance
  "Returns the distance between 2 points in space."
  [p1 p2]
  (+ (abs (- (x-coordinate p1) (x-coordinate p2))) (abs (- (y-coordinate p1) (y-coordinate p2)))))

(defn make-galaxy
  "Creates a representation for a galaxy."
  [x y]
  {:location (make-point x y)})

(defn location
  "Returns the 2D point represeting the location of a given galaxy."
  [g]
  (:location g))

(defn distance-between-galaxies
  "Returns the distance between 2 galaxies."
  [g1 g2] 
  (distance (location g1) (location g2)))

(defn find-all-pairs [elements]
  (combo/combinations elements 2))

(defn shortest-distance-between-galaxies
  "Returns the sum of shortest distances between all the galaxies."
  [galaxies]
  (->> (find-all-pairs galaxies) 
       (map #(distance-between-galaxies (first %) (last %)))
       (reduce +)))


(defn parse-line
  [i line] 
  (->> line
       (filter #(not (clojure.string/blank? %))) 
       (map-indexed (fn [j val] {:x i :y j :sym val}))
       (filter #(= "#" (get % :sym)))))

(defn parse-galaxy
  [v]
  (make-galaxy (:x v) (:y v)))

(defn parse-galaxies
  "Parses a string of input data and returns a sequence of galaxies."
  [input] 
  (->> input
       (map-indexed parse-line)
       (reduce concat) 
       (pmap parse-galaxy) 
       ))

(defn is-space? [s]
  (= s "."))

(defn expansion
  [s expansion-factor]
  (if (every? is-space? s)
    (vec (repeat expansion-factor s))
    [s]))

(defn transpose-space
  [space]
  (apply mapv vector space))

(defn expand-space-vertically
  [input expf]
  (->> input
       (map #(expansion % expf))
       (reduce concat)))

(defn expand-space-horizontally
  [input expf]
  (transpose-space (expand-space-vertically (transpose-space input) expf)))

(defn build-space
  [input]
  (->> (clojure.string/split-lines input)
       (filter #(not (clojure.string/blank? %)))
       (map #(clojure.string/split % #""))))

(defn expand-space
  [input expf]
  (expand-space-horizontally (expand-space-vertically (build-space input) expf) expf))

(defn solve
  [input expf]
  (let [space (expand-space input expf)
        galaxies (parse-galaxies space)]
    (shortest-distance-between-galaxies galaxies)))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve input 2))
    (println "Solution 2:" (solve input 1000000))))