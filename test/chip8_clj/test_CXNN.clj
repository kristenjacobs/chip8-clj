(ns chip8-clj.test-CXNN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest random-bitwise-and-test
  (with-redefs-fn {#'clojure.core/rand-int (fn [n] 0xAA)}
    #(let [pre-machine-state (-> (machine-state/initialise)
                                 (machine-state/set-pc 0)
                                 (machine-state/set-instr 0 0xC10F))
           post-machine-state (step pre-machine-state)]
       (is (= (machine-state/get-register post-machine-state 1) 0x0A))
       (is (= (machine-state/get-pc post-machine-state) 2)))))

