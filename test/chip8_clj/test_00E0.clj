(ns chip8-clj.test-00E0
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest clear-screen-test
 (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x00E0)
                              (machine-state/set-screen-buffer 0 0 1)
                              (machine-state/set-screen-buffer 1 1 1)
                              (machine-state/set-screen-buffer 2 2 1)
                              (machine-state/set-screen-buffer 3 3 1)
                              (machine-state/set-screen-buffer 4 4 1)
                              (machine-state/set-screen-buffer 5 5 1))
        post-machine-state (step pre-machine-state)]

    ; Checks the screen buffer has been cleared.
    (is (= (machine-state/get-screen-buffer post-machine-state 0 0) 0))
    (is (= (machine-state/get-screen-buffer post-machine-state 1 1) 0))
    (is (= (machine-state/get-screen-buffer post-machine-state 2 2) 0))
    (is (= (machine-state/get-screen-buffer post-machine-state 3 3) 0))
    (is (= (machine-state/get-screen-buffer post-machine-state 4 4) 0))
    (is (= (machine-state/get-screen-buffer post-machine-state 5 5) 0))))

