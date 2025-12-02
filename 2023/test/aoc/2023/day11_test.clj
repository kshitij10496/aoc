(ns aoc.2023.day11-test
  (:require [clojure.test :refer :all]
            [clojure.string :as clojure.string]
            [aoc.2023.day11 :as cosmos :refer :all]
            [clojure.set :as clojure.set :refer :all]))

(deftest distance-test
  (is (= 9 (cosmos/distance (cosmos/make-point 1 6) (cosmos/make-point 5 11)))) 
  (is (= 15 (cosmos/distance (cosmos/make-point 4 0) (cosmos/make-point 9 10))))
  (is (= 17 (cosmos/distance (cosmos/make-point 0 2) (cosmos/make-point 12 7))))
  (is (= 5 (cosmos/distance (cosmos/make-point 0 11) (cosmos/make-point 5 11)))))

(deftest distance-between-galaxies-test
  (is (= 9 (cosmos/distance-between-galaxies (cosmos/make-galaxy 1 6) (cosmos/make-galaxy 5 11))))
  (is (= 15 (cosmos/distance-between-galaxies (cosmos/make-galaxy 4 0) (cosmos/make-galaxy 9 10))))
  (is (= 17 (cosmos/distance-between-galaxies (cosmos/make-galaxy 0 2) (cosmos/make-galaxy 12 7))))
  (is (= 5 (cosmos/distance-between-galaxies (cosmos/make-galaxy 0 11) (cosmos/make-galaxy 5 11)))))

(deftest shortest-distance-between-galaxies-test
  (is (= 9 (cosmos/shortest-distance-between-galaxies [(cosmos/make-galaxy 1 6) (cosmos/make-galaxy 5 11)])))
  (is (= 15 (cosmos/shortest-distance-between-galaxies [(cosmos/make-galaxy 4 0) (cosmos/make-galaxy 9 10)])))
  (is (= 17 (cosmos/shortest-distance-between-galaxies [(cosmos/make-galaxy 0 2) (cosmos/make-galaxy 12 7)])))
  (is (= 5 (cosmos/shortest-distance-between-galaxies [(cosmos/make-galaxy 0 11) (cosmos/make-galaxy 5 11)]))))

;; (deftest parse-galaxies-test 
;;   (let [input "....#........
;;                .........#...
;;                #............
;;                .............
;;                .............
;;                ........#....
;;                .#...........
;;                ............#
;;                .............
;;                .............
;;                .........#...
;;                #....#......."]
;;     (is (= (set (cosmos/parse-galaxies input)) #{(cosmos/make-galaxy 0 4)
;;                                            (cosmos/make-galaxy 1 9)
;;                                            (cosmos/make-galaxy 2 0)
;;                                            (cosmos/make-galaxy 5 8)
;;                                            (cosmos/make-galaxy 6 1)
;;                                            (cosmos/make-galaxy 7 12)
;;                                            (cosmos/make-galaxy 10 9)
;;                                            (cosmos/make-galaxy 11 0)
;;                                            (cosmos/make-galaxy 11 5)}))))
  
;; ((deftest parse-line-test
;;       (testing "Examaple"
;;         (is (= (set (cosmos/parse-line 0 "....#........")) #{{:x 0 :y 4, :sym "#"}}))
;;         (is (= (set (cosmos/parse-line 11 "#....#.......")) #{{:x 11 :y 0, :sym "#"} {:x 11 :y 5, :sym "#"}})))))

;; (deftest expand-space-horizontally-test
;;   (testing "example"
;;     (let [input "...#......\n.......#..\n#.........\n..........\n......#...\n.#........\n.........#\n..........\n.......#..\n#...#....."
;;           space (cosmos/build-space input)
;;           expected "....#........\n.........#...\n#............\n.............\n.............\n........#....\n.#...........\n............#\n.............\n.............\n.........#...\n#....#......."]
;;       (is (= (count (clojure.string/split-lines expected)) (count (clojure.string/split-lines (cosmos/expand-space-horizontally space))))))))

;; (deftest expand-space-vertically-test
;;   (testing "example"
;;     (let [input "...#......\n.......#..\n#.........\n..........\n......#...\n.#........\n.........#\n..........\n.......#..\n#...#....."
;;           expected "....#........\n.........#...\n#............\n.............\n.............\n........#....\n.#...........\n............#\n.............\n.............\n.........#...\n#....#......."]
;;       (is (= (count (clojure.string/split-lines expected)) (count (cosmos/expand-space-vertically (cosmos/build-space input))))))))


(deftest build-space-test
  (testing "example"
    (is (= [["a" "b" "c"]
              ["1" "2" "3"]
              ["x" "y" "z"]] (cosmos/build-space "abc\n123\nxyz")))))

(deftest transpose-space-test
  (testing "example"
    (is (= [["a" "1" "x"]
              ["b" "2" "y"]
              ["c" "3" "z"]] (cosmos/transpose-space (cosmos/build-space "abc\n123\nxyz"))))))

;; ((deftest expand-space-test
;;       (testing "Example"
;;         (let [input "...#......\n.......#..\n#.........\n..........\n......#...\n.#........\n.........#\n..........\n.......#..\n#...#....."
;;               expected "....#........\n.........#...\n#............\n.............\n.............\n........#....\n.#...........\n............#\n.............\n.............\n.........#...\n#....#......."]
;;           (is (= (cosmos/build-space expected) (cosmos/expand-space input)))))))

((deftest solve1-test
      (testing "Example"
        (let [input "...#......\n.......#..\n#.........\n..........\n......#...\n.#........\n.........#\n..........\n.......#..\n#...#....."]
          (is (= 374 (cosmos/solve input 2)))
          (is (= 1030 (cosmos/solve input 10)))
          (is (= 8410 (cosmos/solve input 100)))))))
