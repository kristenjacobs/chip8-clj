(ns chip8-clj.graphics
  (:require [clojure.tools.logging :as log]
            [chip8-clj.state :as state])
  (:use seesaw.core
        seesaw.graphics
        seesaw.color))

(def width-pixels 64)
(def height-pixels 32)
(def pixel-width 10)
(def pixel-height 10)

(defn- get-x
  [index]
  (mod index width-pixels))

(defn- get-y
  [index]
  (quot index width-pixels))

(defn get-index
  [x y]
  (assert (and (>= x 0) (< x width-pixels)))
  (assert (and (>= y 0) (< y height-pixels)))
  (let [index (+ (* y width-pixels) x)]
    index))

(defn- get-pixel-in-buffer
  [screen-buffer x y]
  (assert (and (>= x 0) (< x width-pixels)))
  (assert (and (>= y 0) (< y height-pixels)))
  (assoc screen-buffer (get-index x y) value))

(defn- set-pixel-in-buffer
  [screen-buffer x y value]
  (assert (and (>= x 0) (< x width-pixels)))
  (assert (and (>= y 0) (< y height-pixels)))
  (assoc screen-buffer (get-index x y) value))

(defn- set-pixel
  [color c g index]
  (let [x (get-x index)
        y (get-y index)]
    (doto g
      (.setColor color)
      (.fillRect (* x pixel-width) 
                 (* y pixel-height)
                 pixel-width 
                 pixel-height))))

(def set-pixel-black (partial set-pixel java.awt.Color/BLACK))
(def set-pixel-white (partial set-pixel java.awt.Color/WHITE))

(defn- refresh-screen
  [screen-buffer canvas graphics]
  (log/debug "Refreshing screen")
  (doall (map (fn [index pixel] 
                (if (= pixel 1)
                  (set-pixel-white canvas graphics index)
                  (set-pixel-black canvas graphics index)))
              (iterate inc 0) screen-buffer)))

(defn- dump-screen-buffer
  [screen-buffer]
  (doseq [y (rseq (vec (range 0 height-pixels)))]
    (doseq [x (range 0 width-pixels)]
      (print (format "%d" (get screen-buffer (get-index x y)))))
    (println "")))

(defn render-screen-buffer
  [machine-state]
  (let [canvas (:screen machine-state)
        screen-buffer (:screen-buffer machine-state)]
    (config! canvas :paint (partial refresh-screen screen-buffer))
    (repaint! canvas)
    machine-state))

(defn- keychar-to-int
  [c]
  (let [keychars 
        {\0 0
         \1 1
         \2 2
         \3 3
         \4 4
         \5 5
         \6 6
         \7 7
         \8 8
         \9 9
         \a 10
         \b 11
         \c 12
         \d 13
         \e 14
         \f 15}]
    (get keychars c)))

(defn- handle-key-pressed
  [e]
  (let [c (.getKeyChar e)]
    (log/debug "Pressed key:" c)
    (if-let [i (keychar-to-int c)]
      (reset! state/keys-pressed 
               (conj @state/keys-pressed i)))))

(defn- handle-key-released
  [e]
  (let [c (.getKeyChar e)]
    (log/debug "Released key:" c)
    (if-let [i (keychar-to-int c)]
      (reset! state/keys-pressed 
             (disj @state/keys-pressed i)))))

(defn- create-frame
  [cvs]
  (let [f (frame :title "Chip8-clj"
                 :content cvs
                 :width (* pixel-width width-pixels) 
                 :height (+ 15 (* pixel-height height-pixels))
                 :on-close :exit)]
    (listen f :key-pressed handle-key-pressed)
    (listen f :key-released handle-key-released) 
    f))

(defn create-screen
  []
  (log/debug "Creating screen")
  (canvas :id :canvas :paint nil))

(defn create-screen-buffer
  []
  (log/debug "Creating screen buffer")
  (vec (repeat (* height-pixels width-pixels) 0)))

(defn start
  [machine-state]
  (log/debug "Starting graphics")
  (show! (create-frame (:screen machine-state))))

