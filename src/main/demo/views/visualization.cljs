(ns demo.views.visualization
  (:require [reagent.core :as reagent]
            [shadow.loader :as loader]))

(defn load-module [module loaded?]
  (let [set-loaded #(reset! loaded? %)]
    (-> (loader/load module)
        (.then
          #(set-loaded true)
          #(set-loaded :error)))))

(defn render []
  (let [chart-loaded? (reagent/atom false)]
    (reagent/create-class
      {:component-did-mount #(when-not (loader/loaded? "chart")
                               (load-module "chart" chart-loaded?))
       :reagent-render
        (fn []
          (cond
            (= :error @chart-loaded?) [:div "Problem loading chart"]
            (true? @chart-loaded?)
            ((resolve 'demo.views.chart/render))
            :else [:div "Loading chart module."]))})))
