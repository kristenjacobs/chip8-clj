(ns chip8-clj.test-FX0A
  (:require [chip8-clj.machine-state :as machine-state]
            [chip8-clj.state :as state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest wait-key-pressed-test
  (with-redefs-fn {#'state/get-key-pressed (fn [] 22)}
    #(let [pre-machine-state (-> (machine-state/initialise)
                                 (machine-state/set-pc 0)
                                 (machine-state/set-instr 0 0xF10A))
           post-machine-state (step pre-machine-state)]
       (is (= (machine-state/get-register post-machine-state 1) 22))
       (is (= (machine-state/get-pc post-machine-state) 2)))))

