(ns aoc.2023.day24 
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

(defn compute-c
  [m x1 y1]
  (- y1 (* m x1)))

(defn make-line
  [x y vx vy]
  {:m (/ vy vx) :c (compute-c (/ vy vx) x y) :x x :y y :vx vx :vy vy})

(defn slope
  [l]
  (:m l))

(defn x-intercept
  [l]
  (:c l))

(defn begin-x
  [l]
  (:x l))

(defn begin-y
  [l]
  (:y l))

(defn velocity-x
  [l]
  (:vx l))

(defn velocity-y
  [l]
  (:vy l))


(defn point-future?
  [start end velocity]
  (let [t (/ (- end start) velocity)]
    (> t 0)))


(defn future-point?
  [p l]
  (and (point-future? (begin-x l) (x-coordinate p) (velocity-x l))
       (point-future? (begin-y l) (y-coordinate p) (velocity-y l))))

(defn parallel?
  [l1 l2]
  (= (slope l1) (slope l2)))

(defn poi
  [l1 l2] 
  (let [a (slope l1)
        b (slope l2)
        c (x-intercept l1)
        d (x-intercept l2)]
  (make-point (/ (- d c) (- a b))
              (+ c (* a (/ (- d c) (- a b)))))))

(defn poi-future?
  [p l1 l2]
  (and (future-point? p l1) (future-point? p l2)))

(defn build-path
  [x y z, vx, vy, vz]
  (make-line   x y vx vy))

(defn parse-line [s]
  (when (re-matches #"\s*(-?\d+),\s*(-?\d+),\s*(-?\d+)\s+@\s*(-?\d+),\s*(-?\d+),\s*(-?\d+)\s*" s)
     (let [matches (re-matches #"\s*(-?\d+),\s*(-?\d+),\s*(-?\d+)\s+@\s*(-?\d+),\s*(-?\d+),\s*(-?\d+)\s*" s)]
       (if (seq matches)
         (let [x (Long. (nth matches 1))
               y (Long. (nth matches 2))
               z (Long. (nth matches 3))
               vx (Long. (nth matches 4))
               vy (Long. (nth matches 5))
               vz (Long. (nth matches 6))]
           (build-path x y z vx vy vz))))))

(defn within-region?
  [p x-start x-end y-start y-end]
  (let [x (x-coordinate p)
        y (y-coordinate p)]
    (and (>= x x-start) (<= x x-end)
         (>= y y-start) (<= y y-end))))

(defn solve
  [l1 l2 x-start x-end y-start y-end] 
    (if (parallel? l1 l2) false
        (let [p (poi l1 l2)]
          (and (poi-future? p l1 l2)
               (within-region? p x-start x-end y-start y-end)))))
  

(defn find-all-pairs [elements]
  (combo/combinations elements 2))

(defn solve-all
  [lines x-start x-end y-start y-end]
  (count (->> (find-all-pairs lines)
              (map #(solve (first %) (last %) x-start x-end y-start y-end))
              (filter #(true? %)))))

(defn build-lines
  [input]
  (->> (clojure.string/split-lines input)
       (map #(parse-line %))))

(defn solve1
  [input]
  (let [lines (build-lines input)]
    (solve-all lines 200000000000000 400000000000000 200000000000000 400000000000000)))
    ;; (solve-all lines 7 27 7 27)))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))))
    ;; (println "Solution 2:" (solve2 input))))