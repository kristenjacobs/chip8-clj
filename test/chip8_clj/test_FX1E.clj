(ns chip8-clj.test-FX1E
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest memory-to-registers-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xF51E)
                              (machine-state/set-addr-reg 2)
                              (machine-state/set-register 5 2))
        post-machine-state (step pre-machine-state)]

    ; Checks that the address register has been updated.
    (is (= (machine-state/get-addr-reg post-machine-state) 4))

    (is (= (machine-state/get-pc post-machine-state) 2))))

