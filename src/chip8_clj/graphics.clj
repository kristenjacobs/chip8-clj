(ns chip8-clj.graphics
  (:require [clojure.tools.logging :as log]
            [quil.core :as q]
            [chip8-clj.state :as state]))

(def width-pixels 64)
(def height-pixels 32)
(def pixel-width 10)
(def pixel-height 10)

(defn- set-pixel
  [color x y]
  (q/fill color)
  (let [p-x (* x pixel-width)
        p-y (* y pixel-height)]
    (q/rect p-x p-y pixel-width pixel-height)))

(defn- refresh-screen
  []
  (log/debug "Refreshing screen")
  (let [screen-updates (state/fetch-screen-updates)]
    (log/debug screen-updates)
    (doall (map (fn [{:keys [x y bit]}] 
                  (if (= bit 1)
                    (set-pixel 255 x y)
                    (set-pixel 0 x y)))
                screen-updates))))

(defn- keychar-to-int
  [c]
  (let [keychars 
        {\0 0 \1 1 \2 2 \3 3
         \4 4 \5 5 \6 6 \7 7
         \8 8 \9 9 \a 10 \b 11
         \c 12 \d 13 \e 14 \f 15}]
    (get keychars c)))

(defn- handle-key-pressed
  []
  (let [c (q/raw-key)]
    (log/debug "Pressed key:" c)
    (if-let [i (keychar-to-int c)]
      (reset! state/keys-pressed 
               (conj @state/keys-pressed i)))))

(defn- handle-key-released
  []
  (let [c (q/raw-key)]
    (log/debug "Released key:" c)
    (if-let [i (keychar-to-int c)]
      (reset! state/keys-pressed 
             (disj @state/keys-pressed i)))))

(defn- handle-closed
  []
  (System/exit 0))

(defn setup 
  []
  (q/frame-rate 60)  
  (q/background 0))   

(defn start
  []
  (log/debug "Starting graphics")
  (q/defsketch example             
    :title "Chip8-clj"
    :settings #(q/smooth 2)      
    :setup setup                
    :draw refresh-screen    
    :key-pressed handle-key-pressed    
    :key-released handle-key-released    
    :on-close handle-closed
    :size [(* pixel-width width-pixels) 
           (* pixel-height height-pixels)]))

