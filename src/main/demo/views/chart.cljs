(ns demo.views.chart
  (:require [reagent.core :as reagent]
            ["d3" :as d3]))

(def size 600)
(def outer-radius (- (* size 0.5) 40))
(def inner-radius (- outer-radius 30))

(def matrix
  (clj->js
    [[11975 5871 8916 2868]
     [1951 10048 2060 6171]
     [8010 16145 8090 8045]
     [1013 990 940 6907]]))

(def format-value (.formatPrefix d3 ",.0" 1e3))

(def chord
  (-> d3
    (.chord)
    (.padAngle 0.05)
    (.sortSubgroups (.-descending d3))))

(def arc
  (-> d3
    (.arc)
    (.innerRadius inner-radius)
    (.outerRadius outer-radius)))

(def ribbon
  (-> d3
    (.ribbon)
    (.radius inner-radius)))

(def color
  (-> d3
    (.scaleOrdinal)
    (.domain (.range d3 4))
    (.range #js ["#22292F" "#F66D9B" "#6574CD" "#4DC0B5"])))

(defn group-ticks [data step]
  (let [{:keys [endAngle startAngle value]} (js->clj data :keywordize-keys true)
        k (/ (- endAngle startAngle) value)]
    (.map (.range d3 0 value step)
      (fn [v]
        #js {:value v
             :angle (+ startAngle (* v k))}))))

(defn add-datum [svg]
  (-> (.select d3 svg)
    (.append "g")
    (.attr "transform" (str "translate(" (/ size 2) "," (/ size 2) ")"))
    (.datum (chord matrix))))

(defn add-groups [g]
  (let [group (-> (.append g "g")
                (.attr "class" "groups")
                (.selectAll "g")
                (.data #(.-groups %))
                (.enter)
                (.append "g"))]
    (-> group
      (.append "path")
      (.style "fill" (fn [d]
                      (color (.-index d))))
      (.style "stroke" #(-> d3
                          (.rgb (color (.-index %)))
                          (.darker)))
      (.attr "d" arc))
    group))

(defn chart [el]
  (-> el
    (.append "g")
    (.attr "class" "ribbons")
    (.selectAll "path")
    (.data (fn [chords] chords))
    (.enter)
    (.append "path")
    (.attr "d" ribbon)
    (.style "fill" #(color (.. % -target -index)))
    (.style "stroke" #(-> d3
                        (.rgb (color (.. % -target -index)))
                        (.darker)))))

(defn add-group-ticks [group]
  (-> group
    (.selectAll ".group-tick")
    (.data #(clj->js (group-ticks % 1e3)))
    (.enter)
    (.append "g")
    (.attr "class" "group-tick")
    (.attr "transform" (fn [d]
                         (str "rotate(" (- (/ (* (.-angle d) 180) (.-PI js/Math)) 90) ")"
                              "translate(" outer-radius ",0)")))))

(defn add-text [group-tick]
  (-> group-tick
    (.filter #(zero? (mod (.-value %) 5e3)))
    (.append "text")
    (.attr "x" 8)
    (.attr "dy" "0.35em")
    (.attr "transform" #(when (> (.-angle %) (.-PI js/Math))
                          "rotate(180) translate(-16)"))
    (.style "text-anchor" #(when (> (.-angle %) (.-PI js/Math)) "end"))
    (.text #(format-value (.-value %)))))

(defn add-lines [group]
  (-> group
    (.append "line")
    (.style "stroke" (fn [d] "#000000"))
    (.attr "x2" 6)))

(defn add-ribbons [g]
  (-> g
    (.append "g")
    (.style "fill-opacity" 0.67)
    (.selectAll "path")
    (.data identity)
    (.enter)
    (.append "path")
    (.attr "d" ribbon)
    (.style "fill" #(color (.. % -target -index)))
    (.style "stroke" #(-> d3
                        (.rgb (color (.. % -target -index)))
                        (.darker)))))

(defn render []
  (reagent/create-class
    {:component-did-mount (fn [svg]
                            (let [with-datum (add-datum (reagent/dom-node svg))
                                  groups (add-groups with-datum)
                                  group-tick (add-group-ticks groups)]

                              (add-lines group-tick)
                              (add-text group-tick)
                              (add-ribbons with-datum)))
     :reagent-render
      (fn []
        [:svg {:width (str size "px")
               :height (str size "px")}])}))
