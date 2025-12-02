(ns aoc.2023.day14-test
  (:require [clojure.test :refer :all]
             [clojure.string :as clojure.string]
             [aoc.2023.day14 :as reflectors :refer :all]))

(deftest count-rocks-test
      (testing "Context of the test assertions"
        (is (= 5 (reflectors/count-rocks "..O.#O..O.O#..O")))
        (is (= 1 (reflectors/count-rocks "..O.")))
        (is (= 0 (reflectors/count-rocks ".....")))
        ))

(deftest tilt-segment-test
      (testing "Context of the test assertions"
        (is (= "OO..." (reflectors/tilt-segment "..OO.")))
        (is (= "OOO..." (reflectors/tilt-segment "O..O.O")))
        (is (= "OOO..." (reflectors/tilt-segment "O..O.O")))
        (is (= "O.." (reflectors/tilt-segment "..O"))))) 


(deftest segments-test
      (testing "Context of the test assertions"
        (is (= (clojure.string/split "O...#OOO...#O.." #"") (reflectors/segments (clojure.string/split "..O.#O..O.O#..O" #""))))
        (is (= (clojure.string/split "OOOO....##" #"") (reflectors/segments (clojure.string/split "OO.O.O..##" #""))))))

((deftest build-space-test
      (testing "Context of the test assertions"
        (is (= [["O" "." "." "." "." "#" "." "." "." "."] 
                ["O" "." "O" "O" "#" "." "." "." "." "#"] 
                ["." "." "." "." "." "#" "#" "." "." "."] 
                ["O" "O" "." "#" "O" "." "." "." "." "O"] 
                ["." "O" "." "." "." "." "." "O" "#" "."] 
                ["O" "." "#" "." "." "O" "." "#" "." "#"] 
                ["." "." "O" "." "." "#" "O" "." "." "O"] 
                ["." "." "." "." "." "." "." "O" "." "."] 
                ["#" "." "." "." "." "#" "#" "#" "." "."] 
                ["#" "O" "O" "." "." "#" "." "." "." "."]] (reflectors/build-space (clojure.string/split-lines reflectors/foo))))) ))

((deftest transpose-space-test
   (testing "Context of the test assertions"
     (is (= [[\O \O \. \O \. \O \. \. \# \#] 
             [\. \. \. \O \O \. \. \. \. \O] 
             [\. \O \. \. \. \# \O \. \. \O] 
             [\. \O \. \# \. \. \. \. \. \.] 
             [\. \# \. \O \. \. \. \. \. \.] 
             [\# \. \# \. \. \O \# \. \# \#] 
             [\. \. \# \. \. \. \O \. \# \.] 
             [\. \. \. \. \O \# \. \O \# \.] 
             [\. \. \. \. \# \. \. \. \. \.] 
             [\. \# \. \O \. \# \O \. \. \.]] (reflectors/transpose-space (clojure.string/split-lines reflectors/foo)))))))

((deftest build-row-test
      (testing "Context of the test assertions"
        (is (= "OO.O.O..##" (reflectors/build-row [\O \O \. \O \. \O \. \. \# \#]))))))