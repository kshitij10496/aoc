(ns aoc.2023.day12 
  (:require [clojure.string :as clojure.string]
            [clojure.string :as string]))

(defn contiguous-groups
  "Returns a sequence of contiguous groups of damaged springs from a condition record."
  [record]
  (->> record
        (re-seq #"#+")
        (map count)))

(defn replace-with-operational-spring
  "Returns a condition record by updating the first unknown spring with an operational one."
  [record]
  (clojure.string/replace-first record #"\?" "."))

(defn replace-with-damaged-spring
  "Returns a condition record by updating the first unknown spring with an damaged one."
  [record]
  (clojure.string/replace-first record #"\?" "#"))

(defn arrangements
  "Returns the number of arrangements of operational and broken springs that fit the given criteria."
  [record criteria]
  (cond 
    (clojure.string/includes? record "?") (concat (arrangements (replace-with-operational-spring record) criteria) (arrangements (replace-with-damaged-spring record) criteria))
    (= criteria (contiguous-groups record)) [record]))

(defn valid-arrangements
  "Returns the number of valid arrangements of condition record based on the provided criteria."
  [record criteria] 
  (->> (arrangements record criteria)
       (filter #(not-empty %)) 
       (reduce (fn [acc _] (inc acc)) 0)))

(defn parse-criteria
  [s]
  (vec (->> (clojure.string/split s #",")
       (map #(Integer/parseInt %)))))

(defn unfold-record
  [record]
  (clojure.string/join "?" (repeat 5 record)))

(defn unfold-criteria
  [criteria]
  (clojure.string/join "," (repeat 5 criteria)))


(defn parse-condition-record
  "Parses a single line and returns the condition record string and the criteria."
  [line unfold]
  (let [parsed-line (clojure.string/split line #" ")
        record (if unfold (unfold-record (first parsed-line)) (first parsed-line))
        criteria (if unfold (unfold-criteria (last parsed-line)) (last parsed-line))]
    {:record record 
     :criteria (parse-criteria criteria)}))

(defn solve1
  [input]
  (->> (clojure.string/split-lines input) 
       (filter #(not (clojure.string/blank? %)))
       (pmap #(parse-condition-record % false))
       (pmap #(valid-arrangements (:record %) (:criteria %))) 
       (reduce +)))

(defn solve2
  [input]
  (->> (clojure.string/split-lines input)
       (filter #(not (clojure.string/blank? %)))
       (pmap #(parse-condition-record % true))
       (pmap #(valid-arrangements (:record %) (:criteria %)))
       (reduce +)))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))
    (println "Solution 2:" (solve2 input))))
