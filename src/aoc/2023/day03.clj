(ns aoc.2023.day03
  (:require [clojure.string]))

;; Parsing the input
(defn build-number-line [i line]
  ;; Using regex to find all the numbers in a given line.
  ;; The regex is \d+ which means one or more digits.
  ;; It returns a collection of maps of numbers with their indices and values.
  (let [matcher (re-matcher #"\d+" line)]
    (loop [matches []]
      (if (.find matcher)
        (recur (conj matches {:i i
                              :j (.start matcher)
                              :value (Integer/parseInt (.group matcher))
                              :len (count (.group matcher))}))
        matches))))

(defn find-numbers [text]
  ;; Splitting the text into lines, then mapping over the lines to build the numbers.
  ;; Then flattening the result and converting it to a vector.
  (->> text
       (clojure.string/split-lines)
       (map-indexed build-number-line)
       (flatten)
       (vec)))

(defn build-special-symbol-line [i line]
  ;; Using regex to find all the special symbols in a given line.
  ;; The regex is [^\d\.] which means anything but a digit or a dot.
  ;; It returns a collection of maps of special symbols with their indices.
  (let [matcher (re-matcher #"[^\d\.]" line)]
    (loop [matches []]
      (if (.find matcher)
        (recur (conj matches {:i i
                              :j (.start matcher)}))
        matches))))

(defn build-special-symbols [text]
  ;; Splitting the text into lines, then mapping over the lines to build the special symbols.
  ;; Then flattening the result and converting it to a vector. 
  (->> text
       (clojure.string/split-lines)
       (map-indexed build-special-symbol-line)
       (flatten)
       (vec)))

;; Logic
(defn neighbour?
  ;; Given a number and a special symbol, check if the special symbol is adjacent to the number.
  [num symbol]
  (let [i (:i num)
        start (:j num)
        end (+ start (- (:len num) 1))
        si (:i symbol)
        sj (:j symbol)]
    (and (>= sj (dec start))
         (<= sj (inc end))
         (>= si (dec i))
         (<= si (inc i)))))


(defn neighbour-symbols 
  ;; Given a collection of special symbols and a number, return the special symbols adjacent to the number. 
  [symbols num] 
  (filter #(neighbour? num %) symbols))

(defn neighbours
  ;; Given a collection of numbers and a collection of special symbols, return the sum of the numbers adjacent to the special symbols.
  [nums symbols]
  (->> nums 
       (map #(vector % (neighbour-symbols symbols %))) 
       (filter #(not (empty? (second %))))))

(defn nums-neighbouring-special-symbols
  ;; Given a collection of numbers and a collection of special symbols, return the numbers adjacent to the special symbols.
  [nums symbols]
  (->> (neighbours nums symbols)
       (map first)
       (map :value)))

(defn solve-engine
  ;; Given a schematic, return the sum of the numbers adjacent to the special symbols. 
  [schematic]
  (reduce + (nums-neighbouring-special-symbols (find-numbers schematic) (build-special-symbols schematic))))

(defn solve1 [path]
  ;; Solves the first part of the problem.
  (solve-engine (slurp path)))

;; Part 2
(defn build-asterisks [i line]
  ;; Using regex to find all the asterisks (*) in a given line.
  (let [matcher (re-matcher #"[*]" line)] ;; Anything but a digit or a dot.
    (loop [matches []]
      (if (.find matcher)
        (recur (conj matches {:i i
                              :j (.start matcher)}))
        matches))))

(defn find-asterisks [text]
  ;; Splitting the text into lines, then mapping over the lines to build the asterisks.
  ;; Then flattening the result and converting it to a vector.
  (->> text
       (clojure.string/split-lines)
       (map-indexed build-asterisks)
       (flatten)
       (vec)))

(defn gear? [nums]
  (= (count nums) 2))

(defn gear-ratios [nums asterisks]
  ;; Given a collection of numbers and a collection of asterisks, return the product of the gear ratios.
  (->> (neighbours nums asterisks) 
       (filter #(gear? (second %))) 
       (map second)
       (map #(apply * %))))

(defn compute-gears-ratios [schematic]
  ;; Given a schematic, return the sum of all the gear ratios.
  (reduce + (gear-ratios (find-numbers schematic) (find-asterisks schematic))))

(defn solve2 [path]
  ;; Solves the second part of the problem.
  (compute-gears-ratios (slurp path)))

(defn main
    "Main entry point."
    [path]
    (if (empty? path)
        (println "Usage: day03.clj <input-file>") 
        (do
        (println "Part 1:" (solve1 path))
        (println "Part 2:" (solve2 path)))))