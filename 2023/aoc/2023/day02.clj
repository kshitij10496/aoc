(ns aoc.2023.day02
    (:require [clojure.string]))

;; Parsing logic
(defn parse-part [part]
  (let [[match? quantity color] (re-matches #"(\d+) (red|green|blue)" (clojure.string/trim part))]
    [color (Integer. quantity)]))

(defn parse-draw [draw]
  (let [d (into {} (map parse-part (clojure.string/split (clojure.string/trim draw) #",")))]
    [(get d "red" 0) (get d "green" 0) (get d "blue" 0)]))

(defn parse-game
  [line]
  (let [matcher #"Game (\d+):(.*)"
        m (re-matches matcher line)
        draws (clojure.string/split (get m 2) #";")]
    {:id (Integer. (get m 1))
     :draws (map parse-draw draws)}))

;; Part 1

;; Configuration of the bag of cubes in RGB order.
(def CONFIG [12 13 14])

(defn draw-possible? [rgb]
  (and (>= (get CONFIG 0) (get rgb 0))
       (>= (get CONFIG 1) (get rgb 1)) 
       (>= (get CONFIG 2) (get rgb 2))))

(defn game-possible?
  [game]
    (every? draw-possible? (get game :draws)))

(defn possible-games 
  [games] (filter game-possible? games))

(defn sum-ids
  [games]
  (reduce + (map #(:id %) (possible-games games))))

(defn solve1
  [path]
  (sum-ids (map parse-game (clojure.string/split-lines (slurp path)))))

;; Part 2
(defn max-cubes [draws]
  (let [r (apply max (map #(get % 0) draws))
        g (apply max (map #(get % 1) draws))
        b (apply max (map #(get % 2) draws))]
    [r g b]))

(defn cube-product [game]
  (let [draws (get game :draws)]
    (apply * (max-cubes draws))))

(defn sum-cube-products 
  [games] 
  (reduce + (map cube-product games)))

(defn solve2 [path]
  (sum-cube-products (map parse-game (clojure.string/split-lines (slurp path)))))

(defn main
  "Main entry point."
  [path]
  (if (empty? path)
    (println "Usage: day02.clj <input-file>") 
    (do
      (println "Part 1:" (solve1 path))
      (println "Part 2:" (solve2 path)))))
