(ns aoc.2023.day19
    (:require [clojure.string :as clojure.string]))


(defn make-part
  [ x m a s]
  {:x x :m m :a a :s s})

(defn rating-sum
  [part]
  (reduce + (vals part)))

(defn parse-part
  [line]
  (let [pattern #"\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)\}"
        matcher (re-matches pattern line)
        x (Integer. (get matcher 1 0))
        m (Integer. (get matcher 2 0)) 
        a (Integer. (get matcher 3 0)) 
        s (Integer. (get matcher 4 0))]
    (make-part x m a s)))

{:a<2006 #(if (< % 2006) :qkq %)
 :m>2090 #(if (> % 2090) :A %)
 :rfg   #(str "rfg" %)}

(defn make-rule
  [predicate result]
  {:pred predicate :res result})

(defn parse-rule
  [rule]
  (let [parts (clojure.string/split rule #":")]
    (cond 
      (= 0 (count parts)) nil
      (= 1 (count parts)) (make-rule true (last parts))
      :else (make-rule (first parts) (last parts)))))

(defn parse-rules
  [rules]
  (->> (clojure.string/split rules #",")
       (map parse-rule)
       (filter #(not (nil? %)))))

(defn make-workflow
  [ name rules ]
  {:name name :rules rules})

(defn parse-workflow
  [line]
  (let [pattern #"(.*)\{(.*)\}"
        matcher (re-matches pattern line)
        name (get matcher 1)
        rules (parse-rules (get matcher 2))]
    (make-workflow name rules)))

(defn apply-workflow
  [part workflow]
  ()
  )

(defn process-part
  [part worflows]
  (loop [workflow ])
  )
