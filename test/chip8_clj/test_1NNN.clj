(ns chip8-clj.test-1NNN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest instruction-1NNN-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x1246))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 0x246))))

