(ns scratch.client
  (:require
   [com.fulcrologic.fulcro.algorithms.merge :as merge]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.react.version18 :refer [with-react18]]))


(defsc Car [this {:car/keys [id model] :as props}]
  {:query [:car/id :car/model]
   :ident :car/id}
  (dom/div :.m-2
    "Model: " model))
  

(def ui-car (comp/factory Car {:keyfn :car/id}))

(defsc Person [this {:person/keys [id name age cars] :as props}]
  {:query [:person/id :person/name :person/age {:person/cars (comp/get-query Car)}]
   :ident :person/id}
  (dom/div :.m-2
    (dom/div "Name: " name)
    (dom/div "Age: " age)
    (dom/h3 "Cars:")
    (dom/ul
      (map ui-car cars))))

(def ui-person (comp/factory Person {:keyfn :person/id}))

(defsc Sample [this {:root/keys [person]}]
  {:query [{:root/person (comp/get-query Person)}]}
  (dom/div :.container.flex.m-8
    (ui-person person)))

(defonce APP 
  (with-react18 (app/fulcro-app)))

(defn ^:export init []
  (app/mount! APP Sample "app"))

(comment

  (reset! (::app/state-atom APP) {})

  (swap! (::app/state-atom APP) update-in [:person/id 2 :person/age] inc)

  (merge/merge-component! APP Person {:person/id   2
                                      :person/name "Bob"
                                      :person/age  21
                                      :person/cars [{:car/id    1
                                                     :car/model "F-150"}
                                                    {:car/id    2
                                                     :car/model "Escort"}]}
    :replace [:root/person])
    
  (merge/merge-component! APP Person {:person/id   3
                                      :person/name "Jill"
                                      :person/age  21
                                      :person/cars [{:car/id    1
                                                     :car/model "F-150"}
                                                    {:car/id    2
                                                     :car/model "Escort"}]}
    :replace [:root/person])

  (app/current-state APP)
  (app/schedule-render! APP))


