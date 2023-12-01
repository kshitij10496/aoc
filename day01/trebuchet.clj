;; trebuchet.clj

(require '[clojure.string :as str])

(defn get-first-digit 
  "Returns the first digit found in the string."
  [s]
  (Integer. (re-find #"\d" s)))

(defn get-calibration-value 
  "Returns a 2 digit number from the given string.
   The calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number."
  [s]
  (+
   (* 10 (get-first-digit s))
   (get-first-digit (apply str (reverse s)))))

(defn sum-calibrations 
  "Computes the sum of calibrations given the path a file containing the inputs."
  [path] 
  (reduce + (map get-calibration-value (str/split-lines (slurp path)))))
