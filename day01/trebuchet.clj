;; trebuchet.clj

(require '[clojure.string :as str])

(defn normalise 
  "Normalises the input string by replacing spelled out digits into corresponding digits."
  [s]
  (let [digit-map {"one" "1"
                   "two" "2"
                   "three" "3"
                   "four" "4"
                   "five" "5"
                   "six" "6"
                   "seven" "7"
                   "eight" "8"
                   "nine" "9"}
        matcher #"one|two|three|four|five|six|seven|eight|nine"]
    (str/replace s matcher digit-map)))

(defn normalise-rev
  "Normalises the input string by replacing spelled out digits into corresponding digits."
  [s]
  (let [digit-map {"eno" "1"
                   "owt" "2"
                   "eerht" "3"
                   "ruof" "4"
                   "evif" "5"
                   "xis" "6"
                   "neves" "7"
                   "thgie" "8"
                   "enin" "9"}
        matcher #"eno|owt|eerht|ruof|evif|xis|neves|thgie|enin"]
    (str/replace s matcher digit-map)))

(defn first-digit 
  "Returns the first digit found in the string."
  [s]
  (Integer. (re-find #"\d" s)))

(defn calibration-value 
  "Returns a 2 digit number from the given string.
   The calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number."
  [s] 
  (+ (* 10 (first-digit (normalise s))) 
     (first-digit (normalise-rev (apply str (reverse s))))))

(defn sum-calibrations 
  "Computes the sum of calibrations given the path a file containing the inputs."
  [path] 
  (reduce + (map calibration-value (str/split-lines (slurp path)))))

;; debugging
;; (def my-map (map calibration-value (str/split-lines (slurp "input.txt"))))
;; (let [output-string (with-out-str (println my-map))]