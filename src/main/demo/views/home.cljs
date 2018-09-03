(ns demo.views.home
  (:require [demo.routes :as routes]
            [demo.subs :as subs]
            [re-frame.core :as re-frame]))

(defn render []
  [:<>
    [:p "This page is a module, and here's one with a "
      [:a.text-blue.no-underline.hover:underline {:href (routes/visualization)} "d3 js dependency"]]])
