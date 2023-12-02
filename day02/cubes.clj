;; cubes.clj --- Cubes Conundrum

(require '[clojure.string])

;; Configuration of the bag of cubes in RGB order.
(def CONFIG [12 13 14])

;; (defn draw-possible? 
;;   [rgb]
;;   (case (compare CONFIG rgb)
;;     -1 false
;;     0 true
;;     1 true))

(defn draw-possible? [rgb]
  (and (>= (get CONFIG 0) (get rgb 0))
       (>= (get CONFIG 1) (get rgb 1)) 
       (>= (get CONFIG 2) (get rgb 2))))

(defn game-possible?
  [game]
    (every? draw-possible? (get game :draws)))

(defn possible-games 
  [games] (filter game-possible? games))

(defn sum-ids
  [games]
  (reduce + (map #(:id %) games)))

(defn solve
  [games]
  (sum-ids (possible-games games)))

;; (def games [{:id 1, :draws [[4 0 3] [1 2 6] [0 2 0]]}
;;             {:id 2, :draws [[0 2 1] [1 3 4] [0 1 1]]}
;;             {:id 3, :draws [[20 8 6] [4 13 5] [1 5 0]]}
;;             {:id 4, :draws [[3 1 6] [6 3 0] [14 3 15]]}
;;             {:id 5, :draws [[6 3 1] [1 2 2]]}])

(defn parse-part [part]
  (let [[match? quantity color] (re-matches #"(\d+) (red|green|blue)" (clojure.string/trim part) )]
    [color (Integer. quantity)]))

(defn parse-draw [draw]
  (let [d (into {} (map parse-part (clojure.string/split (clojure.string/trim draw) #",")))]
    [(get d "red" 0) (get d "green" 0) (get d "blue" 0)]))

(defn parse-game
  [line]
  (let [matcher #"Game (\d+):(.*)"
        m (re-matches matcher line)
        draws (clojure.string/split (get m 2) #";")]
   {:id (Integer. (get m 1))
    :draws (map parse-draw draws)}))

(defn s [path]
  (solve (map parse-game (clojure.string/split-lines (slurp path)))))

;; (defn parse-rgb-string [input-string]
;;   (let [parse-rgb-part (fn [part]
;;                          (let [[quantity color] (clojure.string/split part #",\s*")]
;;                            (case color
;;                              "red" [quantity 0 0]
;;                              "green" [0 quantity 0]
;;                              "blue" [0 0 quantity])))]
;;     (map (fn [part] (apply vector (sort (parse-rgb-part part)))) (clojure.string/split input-string #";\s*"))))