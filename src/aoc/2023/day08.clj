(ns aoc.2023.day08
  (:require [clojure.string :as clojure.string]
            [clojure.set :as clojure.set]))

(def start-node-label :AAA)
(def destination-node-label :ZZZ)

(defn turn-right
  "Given a network and a node label, return the label of the node to the right of the given node.
  If the node-label does not exist in the network, return nil."
  [network node-label]
  (let [node (first (filter #(= (:label %) node-label) network))]
    (:right node)))

(defn turn-left
  "Given a network and a node label, return the label of the node to the left of the given node.
  If the node-label does not exist in the network, return nil."
  [network node-label]
  (let [node (first (filter #(= (:label %) node-label) network))]
    (:left node)))

(defn navigate-turns
  "Given a network, a starting node label, and a destination node label, return the number of turns required to reach the destination node from the starting node.
  If the destination node cannot be reached from the starting node, return nil."
  [network start-node-label turns steps]
  (loop [current-node-label start-node-label
         turns turns
         steps steps]
    (cond
      (= current-node-label destination-node-label) {:label current-node-label :steps steps}
      (or (nil? turns) (empty? turns)) {:label current-node-label :steps steps}
      :else (recur ((first turns) network current-node-label) (rest turns) (inc steps)))))

(defn navigate
  [network turns start-node-label]
  (loop [start-node-label start-node-label
         steps 0]
    (let [res (navigate-turns network start-node-label turns 0)
          steps-walked (:steps res)
          reached-node-label (:label res)]
      (if (= (:label res) destination-node-label) (+ steps steps-walked)
          (recur reached-node-label (+ steps steps-walked))))))

(defn is-starting-node?
  [node]
  (let [node-label (:label node)]
    (clojure.string/ends-with? (name node-label) "A")))

(defn starting-nodes
  [network]
  (->> network
       (filter is-starting-node?)
       (map :label)
       (set)))

(defn is-destination-node?
  [node-label]
  (clojure.string/ends-with? (name node-label) "Z"))

(defn navigate-from-node
  [network turns start-node-label]
  (loop [start-node-label start-node-label
         turns turns
         nodes-reached []]
    (if (empty? turns) nodes-reached
        (let [reached-node-label ((first turns) network start-node-label)]
          (recur reached-node-label (rest turns) (conj nodes-reached reached-node-label))))))

(defn walk-node-till-success
  [network turns start-node]
  (loop [node start-node
         turns turns
         steps 1]
    (let [result ((first turns) network node)
          success? (is-destination-node? result)] 
      (if success? steps
          (recur result (rest turns) (inc steps))))))

(defn gcd [a b]
  (if (zero? b)
    a
    (recur b (mod a b))))

(defn lcm [a b]
  (/ (Math/abs (* a b)) (gcd a b)))

(defn walk-all-nodes-till-success
  [network turns start-nodes]
  (->> start-nodes
       (map #(walk-node-till-success network turns %))
       (reduce lcm)))

(defn parse-instructions-line
  [line]
  (->> (clojure.string/split (clojure.string/trim line) #"")
        (filter #(not (clojure.string/blank? %)))
        (map #(if (= % "R") :R :L))))

(defn build-turns
  [instructions]
  (->> instructions
        (map #(if (= % :R) turn-right turn-left))))

(defn parse-node-line
  [line]
  (let [matcher #"(\w+) = \((\w+), (\w+)\)"
        parts (re-matches matcher (clojure.string/trim line))
        node-label (keyword (get parts 1))
        left-node-label (keyword (get parts 2))
        right-node-label (keyword (get parts 3))]
    {:label node-label :left left-node-label :right right-node-label}))

(defn build-network
  [lines]
  (->> lines
        (filter #(not (clojure.string/blank? %)))
        (map parse-node-line)
        (set)))

(defn parse-input
  [input]
  (let [lines (clojure.string/split-lines input)
        turns (build-turns (parse-instructions-line (first lines)))
        network (build-network (rest lines))]
    {:turns turns :network network}))

(defn solve1
  [input]
  (let [parsed-input (parse-input input)]
    (navigate (:network parsed-input) (:turns parsed-input) start-node-label)))

(defn solve2
  [input]
  (let [parsed-input (parse-input input)]
    (walk-all-nodes-till-success (:network parsed-input) (cycle (:turns parsed-input)) (starting-nodes (:network parsed-input)))))

(defn main
  [path]
  (let [input (slurp path)]
    (println "Solution 1:" (solve1 input))
    (println "Solution 2:" (solve2 input))))