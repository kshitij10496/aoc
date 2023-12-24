(ns aoc.2023.day18 
  (:require [clojure.string :as clojure.string]))

(defn make-point
  "Creates a 2D point in space with the given x and y co-ordinates"
  [x y] (vector x y))

(defn x-coordinate
  "Returns the x co-ordinate of a 2D point."
  [p] (first p))

(defn y-coordinate
  "Returns the y co-ordinate of a 2D point."
  [p] (last p))

(defn determinant
  [p1 p2] 
  (- (* (x-coordinate p1) (y-coordinate p2)) (* (y-coordinate p1) (x-coordinate p2))))

(defn shoelace
  [p1 & [points]]
  (let [seqa (conj (seq points) p1)
        seqb (conj (vec points) p1)
        area 0]
    (println seqa)
    (println seqb)
    (loop [seqa seqa
           seqb seqb
           area area]
      (if (or (empty? seqa) (empty? seqb))
        (abs (/ area 2.0))
        (recur (rest seqa) (rest seqb) (+ area (determinant (first seqa) (first seqb))))))))

(defn make-instruction
  [dir units & [color]]
  {:dir dir :units units :color color})

(defn dir-instruction
  [instruction]
  (:dir instruction))

(defn units-instruction
  [instruction]
  (:units instruction))

(defn move
  [start dir units]
  (cond
    (= dir "R") (make-point (+ (x-coordinate start) units) (y-coordinate start))
    (= dir "L") (make-point (- (x-coordinate start) units) (y-coordinate start))
    (= dir "U") (make-point (x-coordinate start) (+ (y-coordinate start) units))
    (= dir "D") (make-point (x-coordinate start) (- (y-coordinate start) units))))

(defn polygon
  [instructions]
  (let [start (make-point 0 0)]
    (loop [instructions instructions
           points [start]]
      (if (empty? instructions) points
      (recur (rest instructions) (conj points (move (last points) (dir-instruction (first instructions)) (units-instruction (first instructions)))))))))

(defn build-instruction
  [instruction]
  (let [matcher #"(.) (\d+) \((.*)\)"
        m (re-matches matcher instruction)
        dir (get m 1)
        units (get m 2)
        color (get m 3)]
    (make-instruction dir (Integer. units) color)))


(defn build-points
  [input]
  (->> (clojure.string/split-lines input)
       (filter #(not (clojure.string/blank? %)))
       (map build-instruction)))

(defn solve1
  [input]
  (let [points (polygon (build-points input))]
    (shoelace (first points) (rest points))))

;; (defn solve2
;;   [input]
;;   (let [instructions (build-instructions input)]
;;     (->> (apply-instructions instructions)
;;          (map-indexed (fn [idx box] (box-power (+ idx 1) box)))
;;          (reduce +))))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))))
    ;; (println "Solution 2:" (solve2 input))))