(ns aoc.2023.day15-test
  (:require [clojure.test :refer :all]
            [clojure.string :as clojure.string]
            [aoc.2023.day15 :as lens :refer :all]))

(deftest hash-char-test 
  (testing "Hash of individual characters"
    (is (= 200 (lens/hash-char \H)))
    (is (= 65 (lens/hash-char \1)))
    (is (= 13 (lens/hash-char \=)))
    (is (= 153 (lens/hash-char \A 200)))
    (is (= 172 (lens/hash-char \S 153)))
    (is (= 52 (lens/hash-char \H 172))))) 

(deftest hash-str-test
  (testing "Hash of strings"
    (is (= 0 (lens/hash-str "rn")))
    (is (= 1 (lens/hash-str "qp")))
    (is (= 0 (lens/hash-str "cm")))
    (is (= 3 (lens/hash-str "pc")))
    (is (= 3 (lens/hash-str "ot")))
    (is (= 30 (lens/hash-str "rn=1")))
    (is (= 253 (lens/hash-str "cm-")))
    (is (= 97 (lens/hash-str "qp=3")))
    (is (= 47 (lens/hash-str "cm=2")))
    (is (= 14 (lens/hash-str "qp-")))
    (is (= 180 (lens/hash-str "pc=4")))
    (is (= 9 (lens/hash-str "ot=9")))
    (is (= 197 (lens/hash-str "ab=5")))
    (is (= 48 (lens/hash-str "pc-")))
    (is (= 214 (lens/hash-str "pc=6")))
    (is (= 231 (lens/hash-str "ot=7"))))) 

(deftest hash-sequence-test
  (testing "Hash of initialization sequence"
    (is (= 1320 (lens/hash-sequence "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7")))))

(deftest focusing-power-test
      (testing "Examples"
        (is (= 1 (lens/focusing-power 0 0 1)))
        (is (= 4 (lens/focusing-power 0 1 2)))
        (is (= 28 (lens/focusing-power 3 0 7)))
        (is (= 40 (lens/focusing-power 3 1 5)))
        (is (= 72 (lens/focusing-power 3 2 6))))) 

(deftest remove-lens-test
  (testing "Context of the test assertions"
    (let [box0 (lens/make-box 0 [(lens/make-lens "rn" 1)])]
      (is (lens/eq-box box0 (lens/remove-lens box0 "cm")))))
  (testing "some other case"
    (let [box0 (lens/make-box 0 [(lens/make-lens "rn" 1) (lens/make-lens "cm" 2)])
          box1 (lens/make-box 1 [(lens/make-lens "qp" 3)])]
      (is (lens/eq-box (lens/make-box 0 [(lens/make-lens "rn" 1)]) (lens/remove-lens box0 "cm")))
      (is (lens/eq-box box1 (lens/remove-lens box1 "cm"))))))

(deftest add-lens-test
  (testing "adding lens to empty box"
    (is (lens/eq-box (lens/make-box 0 [(lens/make-lens "rn" 1)]) (lens/add-lens (lens/make-box 0 nil) (lens/make-lens "rn" 1)))))
  
  (testing "adding lens to existing box"
    (is (lens/eq-box (lens/make-box 0 [(lens/make-lens "rn" 1) (lens/make-lens "cm" 2)]) (lens/add-lens (lens/make-box 0 [(lens/make-lens "rn" 1)]) (lens/make-lens "cm" 2)))))
  
  (testing "updating lens power to existing box"
    (is (lens/eq-box (lens/make-box 0 [(lens/make-lens "rn" 1) (lens/make-lens "cm" 5)]) (lens/add-lens (lens/make-box 0 [(lens/make-lens "rn" 1) (lens/make-lens "cm" 2)]) (lens/make-lens "cm" 5))))))

  