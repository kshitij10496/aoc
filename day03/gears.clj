;; gears.clj --- Gear Ratios
(ns gears
  (:require [clojure.string]))

;; (defn get-entry [schematic [i j]]
;;   (nth (nth schematic i) j))

;; (defn is-adjacent-to-middle? [schematic i j]
;;   (or (is-special-symbol? (get-entry schematic [(dec i) (dec j)]))
;;       (is-special-symbol? (get-entry schematic [(dec i) j]))
;;       (is-special-symbol? (get-entry schematic [(dec i) (inc j)]))
;;       (is-special-symbol? (get-entry schematic [i (dec j)]))
;;       (is-special-symbol? (get-entry schematic [i (inc j)]))
;;       (is-special-symbol? (get-entry schematic [(inc i) (dec j)]))
;;       (is-special-symbol? (get-entry schematic [(inc i) j]))
;;       (is-special-symbol? (get-entry schematic [(inc i) (inc j)]))))

;; (defn is-adjacent-top-left? [schematic]
;;   (let [m (count schematic)
;;         n (count (first schematic))]
;;   (or (and (> n 1) (is-special-symbol? (get-entry schematic [0 1])))
;;       (and (> m 1) (is-special-symbol? (get-entry schematic [1 0])))
;;       (and (> m 1) (> n 1) (is-special-symbol? (get-entry schematic [1 1]))))))

;; (defn is-adjacent-top-right? [schematic]
;;   (let [n (count (first schematic))
;;         i 0
;;         j (dec n)]
;;   (or (is-special-symbol? (get-entry schematic [i (dec j)]))
;;       (is-special-symbol? (get-entry schematic [(inc i) (dec j)]))
;;       (is-special-symbol? (get-entry schematic [(inc i) j])))))

;; (defn is-adjacent-bottom-left? [schematic]
;;   (let [m (count schematic)
;;         n (count (first schematic))
;;         i (dec m)
;;         j 0]
;;     (or (is-special-symbol? (get-entry schematic [(dec i) j]))
;;         (is-special-symbol? (get-entry schematic [(dec i) (inc j)]))
;;         (is-special-symbol? (get-entry schematic [i (inc j)])))))

;; (defn is-adjacent-bottom-right? [schematic]
;;   (let [m (count schematic)
;;         n (count (first schematic))
;;         i (dec m)
;;         j (dec n)]
;;     (or (is-special-symbol? (get-entry schematic [i (dec j)]))
;;         (is-special-symbol? (get-entry schematic [(dec i) (dec j)]))
;;         (is-special-symbol? (get-entry schematic [(dec i) j])))))

;; (defn is-adjacent-top? [schematic j]
;;   (or (is-special-symbol? (get-entry schematic [0 (dec j)]))
;;       (is-special-symbol? (get-entry schematic [0 (inc j)]))
;;       (is-special-symbol? (get-entry schematic [1 (dec j)]))
;;       (is-special-symbol? (get-entry schematic [1 j]))
;;       (is-special-symbol? (get-entry schematic [1 (inc j)]))))

;; (defn is-adjacent-bottom? [schematic j]
;;   (let [m (count schematic)
;;         i (dec m)]
;;   (or (is-special-symbol? (get-entry schematic [(dec i) (dec j)]))
;;       (is-special-symbol? (get-entry schematic [(dec i) j]))
;;       (is-special-symbol? (get-entry schematic [(dec i) (inc j)]))
;;       (is-special-symbol? (get-entry schematic [i (dec j)]))
;;       (is-special-symbol? (get-entry schematic [i (inc j)])))))

;; (defn is-adjacent-to-left? [schematic i]
;;   (let [m (count schematic)
;;         n (count (first schematic))
;;         j 0]
;;   (or (is-special-symbol? (get-entry schematic [(dec i) j]))
;;       (is-special-symbol? (get-entry schematic [(dec i) (inc j)]))
;;       (is-special-symbol? (get-entry schematic [i (inc j)]))
;;       (is-special-symbol? (get-entry schematic [(inc i) j]))
;;       (is-special-symbol? (get-entry schematic [(inc i) (inc j)])))))

;; (defn is-adjacent-to-right? [schematic i]
;;   (let [m (count schematic)
;;         n (count (first schematic))
;;         j (dec n)]
;;   (or (is-special-symbol? (get-entry schematic [(dec i) (dec j)]))
;;       (is-special-symbol? (get-entry schematic [(dec i) j]))
;;       (is-special-symbol? (get-entry schematic [i (dec j)]))
;;       (is-special-symbol? (get-entry schematic [(inc i) (dec j)]))
;;       (is-special-symbol? (get-entry schematic [(inc i) j])))))


;; (defn is-adjacent? [schematic i j]
;;   (let [m (count schematic)
;;         n (count (first schematic))]
;;     (cond
;;       (and (= i 0) (= j 0)) (is-adjacent-top-left? schematic)
;;       (and (= i 0) (= j (dec n))) (is-adjacent-top-right? schematic)
;;       (and (= i (dec m)) (= j 0)) (is-adjacent-bottom-left? schematic)
;;       (and (= i (dec m)) (= j (dec n))) (is-adjacent-bottom-right? schematic)
;;       (= i 0) (is-adjacent-top? schematic j)
;;       (= i (dec m)) (is-adjacent-bottom? schematic j)
;;       (= j 0) (is-adjacent-to-left? schematic i)
;;       (= j (dec n)) (is-adjacent-to-right? schematic i) 
;;       :else (is-adjacent-to-middle? schematic i j))))

;; (defn is-adjacent-to-special-symbol?
;;   [schematic i j n]
;;   (is-adjacent? schematic i j))


;; Locate the numbers in the schematics.
;; Filter out the numbers which are not adjacent to special symbols.
;; (defn find-numbers [schematic]
;;   (doseq [[i row] (map-indexed vector schematic)]
;;      (doseq [[j col] (map-indexed vector row)]
;;        {:i i
;;         :j j
;;         :val col
;;         :is-adjacent (is-adjacent-to-special-symbol? schematic i j col)})))


(def input "467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..")

;; Parsing the input
(defn build-number-line [i line]
    (let [matcher (re-matcher #"\d+" line)]
      (loop [matches []]
        (if (.find matcher)
          (recur (conj matches {
                                :i i
                                :j (.start matcher)
                                :value (Integer/parseInt (.group matcher))
                                :len (count (.group matcher))
                                }))
          matches))))

(defn build-special-symbol-line [i line]
  (let [matcher (re-matcher #"[^\d\.]" line)] ;; Anything but a digit or a dot.
    (loop [matches []]
      (if (.find matcher)
        (recur (conj matches {:i i
                              :j (.start matcher)}))
        matches))))

(defn build-numbers [text]
  (->> text
       (clojure.string/split-lines)
       (map-indexed build-number-line)
       (flatten)
       (vec)))

(defn build-special-symbols [text]
  (->> text
       (clojure.string/split-lines)
       (map-indexed build-special-symbol-line)
       (flatten)
       (vec)))

(defn is-adjacent-special-symbol? 
  [i start end si sj]
  (and (>= sj (dec start))
       (<= sj (inc end))
       (>= si (dec i))
       (<= si (inc i))))

(defn adjacent-symbol? [symbols, num]
  (let [i (:i num)
        start (:j num)
        end (+ start (- (:len num) 1))]
    (filter #(is-adjacent-special-symbol? i start end (:i %) (:j %)) symbols)))

(defn adjacent-nums [nums symbols]
  (->> nums 
       (map #(vector % (adjacent-symbol? symbols %))) 
       (filter #(not (empty? (second %))))
        (map first)
       (map :value)))

(defn solve-engine
    [schematic]
    (reduce + (adjacent-nums (build-numbers schematic) (build-special-symbols schematic))))

(defn solve1 [path]
  (solve-engine (slurp path)))