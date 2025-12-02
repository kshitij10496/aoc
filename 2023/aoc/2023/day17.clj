(ns aoc.2023.day17
  (:require [clojure.data.priority-map :refer [priority-map]]) 
  (:require [clojure.string :as clojure.string]))

(defn make-city-block
  "Creates a 2D point in space with the given x and y co-ordinates"
  [x y heat-loss] 
  {:x x :y y :loss heat-loss})

(defn city-block-x-coordinate
  "Returns the x co-ordinate of a 2D point."
  [block] (:x block))

(defn city-block-y-coordinate
  "Returns the y co-ordinate of a 2D point."
  [block] (:y block))

(defn city-block-heat-loss
  [block] (:loss block))

(defn parse-line
  [i line]
  (->> (clojure.string/split line #"")
       (map-indexed (fn [j loss] (make-city-block i j (Integer. loss))))))

(defn parse-input
  [input]
  (->> (clojure.string/split-lines input)
       (filter #(not (clojure.string/blank? %)))
       (map-indexed parse-line)))

(defn get-block
  [blocks i j] 
  ;; (println (flatten blocks))
  (filter #(and (= (city-block-x-coordinate %) i) (= (city-block-y-coordinate %) j)) (flatten blocks)))

(defn get-north-neighbour
  [blocks i j]
  (if (> i 0) 
    (get-block blocks (- i 1) j)
    nil))

(defn get-south-neighbour
  [blocks i j]
  (if (< i (- (count blocks) 1))
    (get-block blocks (+ i 1) j)
    nil))

(defn get-east-neighbour
  [blocks i j]
  (if (> j 0)
    (get-block blocks i (- j 1))
    nil))

(defn get-west-neighbour
  [blocks i j]
  (if (< j (- (count (first blocks)) 1))
    (get-block blocks i (+ j 1))
    nil))

(defn find-all-neighbours
  [blocks block & [counter direction]]
  (let [i (city-block-x-coordinate block)
        j (city-block-y-coordinate block)
        counter (if (nil? counter) 0 counter)]
  (->> (vector (if (and (= direction "N") (>= counter 3)) nil (get-north-neighbour blocks i j))
               (if (and (= direction "S") (>= counter 3)) nil (get-south-neighbour blocks i j))
               (if (and (= direction "E") (>= counter 3)) nil (get-east-neighbour blocks i j))
               (if (and (= direction "W") (>= counter 3)) nil (get-west-neighbour blocks i j)))
       (filter #(not (nil? %))))))

;; (defn updated-heat-loss
;;   [block accum-heat-loss]
;;   (min accum-heat-loss )
;;   )

;; (defn updated-heat-loss
;;   [blocks p block accum-heat-loss]
;;   (let [neighbours (find-all-neighbours blocks block)]
;;     (->> (neighbours
;;          (assoc p % #(updated-loss % accum-heat-loss))))))

;; (defn dijkstra
;;   [blocks start destination]
;;   ;; Create a priority queue with the starting block set to the accumulated loss value of 0.
;;   ;; Set the distance to every block to Infinity from start in the priority queue.
;;   ;; Pick the first block from the priority queue.
;;   ;; Find all it's neighbours based on the direction constraints.
;;   ;; Update the heat loss value of all the neighbourss based on Dijkstra's algorithm.
;;   ;; Remove the element from the priority queue and put it into a set of visited blocks.
;;   ;; Iterate till we hit the destination block.
;;   (let [p (priority-map start 0)
;;         visited-blocks #{}]
;;     (for [block (flatten blocks)]
;;       (assoc p block Integer/MAX_VALUE))
;;     (loop [p p
;;            visited-blocks visited-blocks
;;            ]
;;       (let [b (first p)
;;             block (key b) 
;;             accum-heat-loss (val b)]
;;         (if (= block destination)
;;           accum-heat-loss
;;           (recur (updated-heat-loss blocks p block accum-heat-loss) (conj visited-blocks block)))))))

(defn solve1
  [input]
  (let [city-blocks (parse-input input)] 
    (find-all-neighbours city-blocks 5 5 3 "W")))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))))