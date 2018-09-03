(ns demo.views
  (:require [demo.routes :as routes]
            [demo.subs :as subs]
            [re-frame.core :as re-frame]
            [demo.views.home]
            [demo.views.visualization]))

(defn page-view [content]
  [:div.flex.flex-col.items-center.text-center.min-h-screen
   [:header.bg-grey-lightest.w-full.p-8
     [:ul.list-reset.flex.items-stretch
      [:li.mx-2 [:a.text-blue-darker.no-underline.hover:underline {:href (routes/home)} "Home"]]
      [:li.mx-2 [:a.text-blue-darker.no-underline.hover:underline {:href (routes/visualization)} "Visualization"]]
      [:li.mx-2.flex-grow.text-right [:a.text-blue-darker.no-underline.hover:underline {:href "https://github.com/jonathandale/re-frame-code-splitting"} "Github"]]]]
   [:main.w-full.flex-grow.flex.items-center.justify-center.p-8 content]])

(defn app-view [{:keys [page-id]}]
  [page-view
    (case page-id
      :home [demo.views.home/render]
      :visualization [demo.views.visualization/render])])

(defn app-root []
  (app-view @(re-frame/subscribe [::subs/app-view])))
