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

(deftest solve1-test
  (testing "Hash of initialization sequence"
    (is (= 1320 (lens/solve1 "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7")))))

(deftest focusing-power-test
      (testing "Examples"
        (is (= 1 (lens/focusing-power 1 1 1)))
        (is (= 4 (lens/focusing-power 1 2 2)))
        (is (= 28 (lens/focusing-power 4 1 7)))
        (is (= 40 (lens/focusing-power 4 2 5)))
        (is (= 72 (lens/focusing-power 4 3 6))))) 

(deftest remove-lens-test
  (testing "Removing non-existing lens from a box"
    (let [box [(lens/make-lens "rn" 1)]
          lens (lens/make-lens "cm")
          res (lens/remove-lens box lens)]
      (is (= 1 (count res)))
      (is (lens/contains-lens? res (first box)))
      ))
  (testing "Removing the only element from a box"
    (let [box [(lens/make-lens "qp" 3)]
          lens (lens/make-lens "qp")]
      (is (= 0 (count (lens/remove-lens box lens))))))
  (testing "Removing the first element from a box"
    (let [box [(lens/make-lens "pc" 4) (lens/make-lens "ot" 9) (lens/make-lens "ab" 5)]
          lens (lens/make-lens "pc")]
      (is (= 2 (count (lens/remove-lens box lens)))))))

(deftest update-lens-test
  (testing "Updating the power of lens in a box"
    (let [box [(lens/make-lens "rn" 1)]
          lens (lens/make-lens "rn" 6)
          res (lens/updated-lens box lens)]
      (is (= 1 (count res )))
      (is (= 6 (lens/lens-power (first res))))
      (is (lens/contains-lens? res lens))))
  
  (testing "Updating in a list of lens"
    (let [box [(lens/make-lens "pc" 4) (lens/make-lens "ot" 9) (lens/make-lens "ab" 5) ]
          lens (lens/make-lens "ot" 6)
          res (lens/updated-lens box lens)]
      (is (= 3 (count res)))
      (is (= 6 (lens/lens-power (second (lens/updated-lens box lens)))))))
  
  (testing "Do not do anything if the lens does not exist in the list"
    (let [box [(lens/make-lens "pc" 4) (lens/make-lens "ot" 9) (lens/make-lens "ab" 5)]
          lens (lens/make-lens "rn" 6)
          res (lens/updated-lens box lens)]
      (is (= 3 (count res)))
      (is (not (lens/contains-lens? res lens))))))

(deftest add-lens-test
  (testing "adding lens to empty box" 
      (let [box []
            lens (lens/make-lens "rn" 6)
            new-box (lens/add-lens box lens)]
        (is (= 1 (count new-box ))) 
        (is (lens/contains-lens? new-box lens))))

  (testing "adding lens to existing box"
    (let [box [(lens/make-lens "rn" 1)]
          lens (lens/make-lens "cm" 2)
          new-box (lens/add-lens box lens)]
      (is (= 2 (count new-box)))
      (is (= 2 (lens/lens-power (last new-box))))
      (is (lens/contains-lens? new-box lens))
      (is (lens/contains-lens? new-box (first box))))))



(deftest upsert-lens-test
  (testing "adding lens to empty box"
    (let [box []
          lens (lens/make-lens "rn" 6)
          new-box (lens/upsert-lens box lens)]
      (is (= 1 (count new-box)))
      (is (lens/contains-lens? new-box lens))))

  (testing "adding lens to existing box"
    (let [box [(lens/make-lens "rn" 1)]
          lens (lens/make-lens "cm" 2)
          new-box (lens/upsert-lens box lens)]
      (is (= 2 (count new-box)))
      (is (= 1 (lens/lens-power (first new-box))))
      (is (= 2 (lens/lens-power (last new-box))))
      (is (lens/contains-lens? new-box lens))
      (is (lens/contains-lens? new-box (first box))))))


(deftest operate-box-test
  (testing "Adding a new lens"
    (let [box [] 
          lens (lens/make-lens "rn" 1) 
          res [(lens/make-lens "rn" 1)]]
       (is (= 1 (count (lens/operate-box box lens false))))
       (is (lens/contains-lens? res lens)))
    ))

(deftest parse-instruction-test
  (testing "Upsert instruction"
    (let [instruction "rn=1"
          res (lens/parse-instruction instruction)]
      (is (= "rn" (:label res)))
      (is (not (:removal? res)))
      (is (= 1 (:power res)))))
  
  (testing "Removal instruction"
    (let [instruction "cm-"
          res (lens/parse-instruction instruction)]
      (is (= "cm" (:label res)))
      (is (:removal? res))
      (is (= 0 (:power res))))))

(deftest solve2-test
  (testing "Example"
    (is (= 1 (lens/solve2 "rn=1")))
    (is (= 1 (lens/solve2 "rn=1,cm-")))
    (is (= 7 (lens/solve2 "rn=1,cm-,qp=3")))
    (is (= 11 (lens/solve2 "rn=1,cm-,qp=3,cm=2")))
    (is (= 145 (lens/solve2 "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7")))))

(deftest build-instructions-test
  (testing "example"
    (is (= 1 (count (lens/build-instructions "rn=1"))))
    (is (= 2 (count (lens/build-instructions "rn=1,cm-"))))))