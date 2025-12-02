(ns aoc.2023.day12-test
  (:require [clojure.test :refer :all]
            [clojure.string :as clojure.string]
            [aoc.2023.day12 :as springs :refer :all]))

(deftest contiguous-groups-test
  (testing "Example strings"
    (is (= [1 1 3] (springs/contiguous-groups "#.#.###")))
    (is (= [1 1 3] (springs/contiguous-groups ".#...#....###.")))
    (is (= [1 3 1 6] (springs/contiguous-groups ".#.###.#.######")))
    (is (= [4 1 1] (springs/contiguous-groups "####.#...#...")))
    (is (= [1 6 5] (springs/contiguous-groups "#....######..#####.")))
    (is (= [3 2 1] (springs/contiguous-groups ".###.##....#")))))

(deftest replace-with-operational-spring-test
  (testing "Context of the test assertions"
    (let [input "???.###"]
      (is (= ".??.###" (springs/replace-with-operational-spring input)))
      (is (= "..?.###" (springs/replace-with-operational-spring (springs/replace-with-operational-spring input))))
      (is (= "....###" (springs/replace-with-operational-spring (springs/replace-with-operational-spring (springs/replace-with-operational-spring input))))))))

(deftest replace-with-damaged-spring-test
  (testing "Context of the test assertions"
    (let [input "???.###"]
      (is (= "#??.###" (springs/replace-with-damaged-spring input)))
      (is (= "##?.###" (springs/replace-with-damaged-spring (springs/replace-with-damaged-spring input))))
      (is (= "###.###" (springs/replace-with-damaged-spring (springs/replace-with-damaged-spring (springs/replace-with-damaged-spring input))))))))

;; (deftest arrangements-test
;;    (testing "Context of the test assertions"
;;      (is (= 8 (count (springs/arrangements "???.###"))))
;;      (is (= 32 (count (springs/arrangements ".??..??...?##."))))
;;      (is (= 256 (count (springs/arrangements "?#?#?#?#?#?#?#?"))))
;;      (is (= 16 (count (springs/arrangements "????.#...#..."))))
;;      (is (= 16 (count (springs/arrangements "????.######..#####.")))) 
;;      (is (= 512 (count (springs/arrangements "?###????????"))))))

(deftest valid-arrangements-test
  (testing "Without unfolding"
    (is (= 1 (springs/valid-arrangements "???.###" [1 1 3])))
    (is (= 4 (springs/valid-arrangements ".??..??...?##." [1 1 3])))
    (is (= 1 (springs/valid-arrangements "?#?#?#?#?#?#?#?" [1 3 1 6])))
    (is (= 1 (springs/valid-arrangements "????.#...#..." [4 1 1])))
    (is (= 4 (springs/valid-arrangements "????.######..#####." [1 6 5])))
    (is (= 10 (springs/valid-arrangements "?###????????" [3 2 1]))))

  (testing "With unfolding"
    (is (= 1 (springs/valid-arrangements (springs/unfold-record "???.###") (apply concat (repeat 5 [1 1 3])))))
    (is (= 16384 (springs/valid-arrangements (springs/unfold-record ".??..??...?##.") (apply concat (repeat 5 [1 1 3])))))
    (is (= 1 (springs/valid-arrangements (springs/unfold-record "?#?#?#?#?#?#?#?") (apply concat (repeat 5 [1 3 1 6])))))
    (is (= 16 (springs/valid-arrangements (springs/unfold-record "????.#...#...") (apply concat (repeat 5 [4 1 1])))))
    (is (= 2500 (springs/valid-arrangements (springs/unfold-record "????.######..#####.") (apply concat (repeat 5 [1 6 5])))))
    (is (= 506250 (springs/valid-arrangements (springs/unfold-record "?###????????") (apply concat (repeat 5 [3 2 1])))))))

(deftest parse-criteria-test
  (is (= [1 1 3] (springs/parse-criteria "1,1,3")))
  (is (= [1 3 1 6] (springs/parse-criteria "1,3,1,6")))
  (is (= [4 1 1] (springs/parse-criteria "4,1,1")))
  (is (= [1 6 5] (springs/parse-criteria "1,6,5")))
  (is (= [3 2 1] (springs/parse-criteria "3,2,1"))))

((deftest parse-condition-record-test
   (testing "Context of the test assertions"
     (is (= {:record "#.#.###" :criteria [1 1 3]} (springs/parse-condition-record "#.#.### 1,1,3" false))))))

(deftest solve-test
  (testing "example input"
    (let [input "???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1"]
      (is (= 21 (springs/solve1 input))))))

(deftest unfold-record-test
  (testing "Context of the test assertions"
    (is (= ".#?.#?.#?.#?.#" (springs/unfold-record ".#")))
    (is (= "???.###????.###????.###????.###????.###" (springs/unfold-record "???.###")))))


(deftest unfold-criteria-test
  (testing "Context of the test assertions"
    (is (= "1,1,1,1,1" (springs/unfold-criteria "1")))
    (is (= "1,1,3,1,1,3,1,1,3,1,1,3,1,1,3" (springs/unfold-criteria "1,1,3")))))
