(ns aoc.2023.day08-test
  (:require [clojure.test :refer :all]
             [aoc.2023.day08 :as wasteland :refer :all]
            [clojure.set :as clojure.set :refer :all]
            ))

(deftest turn-right-test
  (let [test-network (set [{:label :AAA :left :BBB  :right :CCC}
                           {:label :BBB :left :DDD :right :EEE}
                           {:label :CCC :left :ZZZ :right :GGG}])] 
        (is (= :CCC (wasteland/turn-right test-network :AAA)))))

(deftest turn-left-test
  (let [test-network (set [{:label :AAA :left :BBB  :right :CCC}
                           {:label :BBB :left :DDD :right :EEE}
                           {:label :CCC :left :ZZZ :right :GGG}])]
    (is (= :ZZZ (wasteland/turn-left test-network :CCC)))))

(deftest navigate-test
  (testing "Example 1")
  (let [test-network (set [{:label :AAA :left :BBB  :right :CCC}
                           {:label :BBB :left :DDD :right :EEE}
                           {:label :CCC :left :ZZZ :right :GGG}])]
    (is (= 2 (wasteland/navigate test-network [wasteland/turn-right wasteland/turn-left] :AAA))))
  
  (testing "Example 2")
  (let [test-network (set [{:label :AAA :left :BBB  :right :BBB}
                           {:label :BBB :left :AAA :right :ZZZ}
                           {:label :ZZZ :left :ZZZ :right :ZZZ}])]
    (is (= 6 (wasteland/navigate test-network [wasteland/turn-left wasteland/turn-left wasteland/turn-right] :AAA)))))

(deftest parse-instructions-line-test 
    (is (= [:R :L] (wasteland/parse-instructions-line "RL")))
    (is (= [:L :L :R] (wasteland/parse-instructions-line "LLR"))))

((deftest build-turns-test
      (testing "Context of the test assertions"
        (is (= [wasteland/turn-right wasteland/turn-left] (wasteland/build-turns [:R :L])))
        (is (= [wasteland/turn-left  wasteland/turn-left wasteland/turn-right] (wasteland/build-turns [:L :L :R]))))))

(deftest parse-node-line-test
  (is (= {:label :AAA :left :BBB :right :CCC} (wasteland/parse-node-line "AAA = (BBB, CCC)")))
  (is (= {:label :BBB :left :DDD :right :EEE} (wasteland/parse-node-line "BBB = (DDD, EEE)")))
  (is (= {:label :CCC :left :ZZZ :right :GGG} (wasteland/parse-node-line "CCC = (ZZZ, GGG)"))))

(deftest build-network-test
  (let [input ["AAA = (BBB, BBB)" "BBB = (AAA, ZZZ)" "ZZZ = (ZZZ, ZZZ)"]]
    (is (= (set [{:label :AAA :left :BBB :right :BBB}
            {:label :BBB :left :AAA :right :ZZZ}
            {:label :ZZZ :left :ZZZ :right :ZZZ}]) (wasteland/build-network input)))))

(deftest parse-input-test
  (let [input "LLR
               AAA = (BBB, BBB)
               BBB = (AAA, ZZZ)
               ZZZ = (ZZZ, ZZZ)"
        res (wasteland/parse-input input)]
    (is (= (set [{:label :AAA :left :BBB :right :BBB}
                 {:label :BBB :left :AAA :right :ZZZ}
                 {:label :ZZZ :left :ZZZ :right :ZZZ}]) (:network res)))
    (is (= [wasteland/turn-left wasteland/turn-left wasteland/turn-right] (:turns res)))))

(deftest solve1-test
  (testing "Example Case 1"
    (let [input "RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)"]
      (is (= 2 (wasteland/solve1 input)))
      ))
  (testing "Example Case 2"
    (let [input "LLR
               AAA = (BBB, BBB)
               BBB = (AAA, ZZZ)
               ZZZ = (ZZZ, ZZZ)"]
      (is (= 6 (wasteland/solve1 input))))))

(deftest starting-nodes-test
  (testing "Example Case 1"
    (let [test-network (set [{:label :11A :left :11B  :right :XXX}
                             {:label :11B :left :XXX :right :11Z}
                             {:label :11Z :left :11B :right :XXX}
                             {:label :22A :left :22B :right :XXX}
                             {:label :22B :left :22C :right :22C}
                             {:label :22C :left :22Z :right :22Z}
                             {:label :22Z :left :22B :right :22B}
                             {:label :XXX :left :XXX :right :XXX}])]
      (is (= #{:11A :22A} (wasteland/starting-nodes test-network))))))

(deftest is-starting-node?-test
  (testing "Example Case 1"
    (let [test-node {:label :11A :left :11B  :right :XXX}]
      (is (= true (wasteland/is-starting-node? test-node))))))

(deftest is-destination-node?-test
  (testing "Example Case 1" 
    (is (= true (wasteland/is-destination-node? :11Z)))))

(deftest navigate-from-node-test
  (testing "Example Case 1"
    (let [network (set [{:label :11A :left :11B  :right :XXX}
                        {:label :11B :left :XXX :right :11Z}
                        {:label :11Z :left :11B :right :XXX}
                        {:label :22A :left :22B :right :XXX}
                        {:label :22B :left :22C :right :22C}
                        {:label :22C :left :22Z :right :22Z}
                        {:label :22Z :left :22B :right :22B}
                        {:label :XXX :left :XXX :right :XXX}])
          turns [wasteland/turn-left wasteland/turn-right]]
      (is (= [:11B :11Z] (wasteland/navigate-from-node network turns :11A)))
      (is (= [:22B :22C] (wasteland/navigate-from-node network turns :22A))))))

(deftest walk-node-till-success-test
  (testing "Example Case"
    (let [network (set [{:label :11A :left :11B  :right :XXX}
                        {:label :11B :left :XXX :right :11Z}
                        {:label :11Z :left :11B :right :XXX}
                        {:label :22A :left :22B :right :XXX}
                        {:label :22B :left :22C :right :22C}
                        {:label :22C :left :22Z :right :22Z}
                        {:label :22Z :left :22B :right :22B}
                        {:label :XXX :left :XXX :right :XXX}])
          turns [wasteland/turn-left wasteland/turn-right]]
      (is (= 2 (wasteland/walk-node-till-success network (cycle turns) :11A)))
      (is (= 3 (wasteland/walk-node-till-success network (cycle turns) :22A))))))


(deftest walk-all-nodes-till-success-test
  (testing "Example Case"
     (let [network (set [{:label :11A :left :11B  :right :XXX}
                         {:label :11B :left :XXX :right :11Z}
                         {:label :11Z :left :11B :right :XXX}
                         {:label :22A :left :22B :right :XXX}
                         {:label :22B :left :22C :right :22C}
                         {:label :22C :left :22Z :right :22Z}
                         {:label :22Z :left :22B :right :22B}
                         {:label :XXX :left :XXX :right :XXX}])
           turns [wasteland/turn-left wasteland/turn-right]
           start-nodes #{:11A :22A}]
       (is (= 6 (wasteland/walk-all-nodes-till-success network (cycle turns) start-nodes))))))