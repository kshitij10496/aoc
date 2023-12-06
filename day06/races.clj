;; races.clj --- Scratchcards
(ns races
  (:require [clojure.string]))

(defn distance [v t]
  (* v t))

(defn strategies [t]
  (->> (range (+ t 1))
       (map #(distance % (- t %)))))

(defn count-elements-greater-than [sequence value]
  (->> sequence
       (filter #(> % value))
       (count)))

(defn successful-strategies [t d]
  (count-elements-greater-than (strategies t) d))

(def input "Time:      7  15   30
Distance:  9  40  200")

;; Parse input
(defn parse-times [line]
  (let [matcher #"Time: (.*)"
        m (re-matches matcher line)]
    (->> (clojure.string/split (get m 1) #" ")
         (filter #(not (clojure.string/blank? %)))
         (map #(Integer. %)))))

(defn parse-distances [line]
  (let [matcher #"Distance: (.*)"
        m (re-matches matcher line)]
    (->> (clojure.string/split (get m 1) #" ")
         (filter #(not (clojure.string/blank? %)))
         (map #(Integer. %)))))

(defn engine [times, distances]
  (reduce * (map successful-strategies times distances)))

(defn solve1 [path]
  (let [input (clojure.string/split-lines (slurp path))
        times (parse-times (first input))
        distances (parse-distances (second input))]
    (engine times distances)))

;; (races/solve1 "day06/input.txt")


;; Part 2

(defn parse-times-kern [line]
  (let [matcher #"Time: (.*)"
        m (re-matches matcher line)
        s (clojure.string/trim (get m 1))
        t (clojure.string/replace s #"\s" "")] 
    [(Long/parseLong t)]))

(defn parse-distances-kern [line]
  (let [matcher #"Distance: (.*)"
        m (re-matches matcher line)
        s (clojure.string/trim (get m 1))
        d (clojure.string/replace s #"\s" "")]
    [(Long/parseLong d)]))

(defn solve2 [path]
  (let [input (clojure.string/split-lines (slurp path))
         time (parse-times-kern (first input))
         distance (parse-distances-kern (second input))]
     (engine time distance)))

;; (races/solve2 "day06/input.txt")