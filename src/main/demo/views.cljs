(ns demo.views
  (:require [demo.routes :as routes]
            [demo.subs :as subs]
            [re-frame.core :as re-frame]
            [demo.views.home]
            [demo.views.visualization]))

(defn page-view [content]
  [:div.flex.flex-col.items-center.text-center.min-h-screen
   [:header.bg-blue-darker.w-full.p-8
     [:ul.list-reset.flex.justify-center
      [:li.mx-2 [:a.text-grey-light.no-underline {:href (routes/home)} "Home"]]
      [:li.mx-2 [:a.text-grey-light.no-underline {:href (routes/visualization)} "Visualization"]]]]
   [:main.w-full.bg-grey-lightest.flex-grow.flex.items-center.justify-center.p-8 content]
   [:footer.w-full.p-6]])

(defn app-view [{:keys [page-id]}]
  [page-view
    (case page-id
      :home [demo.views.home/render]
      :visualization [demo.views.visualization/render])])

(defn app-root []
  (app-view @(re-frame/subscribe [::subs/app-view])))
