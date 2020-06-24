import netscape.javascript.JSObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        //TODO: write code here
        List<Item> allItem = this.itemRepository.findAll();
        List<SalesPromotion> allPromotion = this.salesPromotionRepository.findAll();
        SelectNum selectNum = new SelectNum();
        selectNum.setItems(inputs);

        ArrayList itemId = new ArrayList();
        for (String input: inputs) {
            String[] inputSp = input.split("x");
            itemId.add(inputSp[0].trim());
        }

        double firstPrice = 0;
        double secondPrice = 0;
        String selectItem="",selectPro="";
        int total = 0;
        String firstSelectItem="";
        String secondSelectPro="";
        int halfPrice = 0;
        String secondProItem = "";
        for(Item item:allItem) {
           for(Object id:itemId) {
               if(id.equals(item.getId())) {
                   double firstItemPrice = item.getPrice()*selectNum.getNum((String)id);
                   firstPrice = firstPrice + item.getPrice()*selectNum.getNum((String)id);
                   firstSelectItem +=  item.getName() + " x "+selectNum.getNum((String)id) +" = "+ (int)firstItemPrice+" yuan\n";
                   if(allPromotion.get(1).getRelatedItems().contains(id)){
                       secondPrice += item.getPrice()/2*selectNum.getNum((String)id);
                       secondProItem += item.getName() + ",";
                       halfPrice += item.getPrice()/2*selectNum.getNum((String)id);
                   }else{
                       secondPrice += item.getPrice()*selectNum.getNum((String)id);
                   }
               }
           }
        }
        secondSelectPro = String.join("，",secondProItem.split(","));
        firstPrice = firstPrice>=30 ? firstPrice-6: firstPrice;

        if (firstPrice<secondPrice) {
            selectItem = firstSelectItem;
            selectPro = "Promotion used:\n"+"满30减6 yuan，saving 6 yuan\n"+"-----------------------------------\n";
            total = (int)firstPrice;
        } else if(firstPrice>secondPrice){
            selectItem = firstSelectItem;
            selectPro = "Promotion used:\n"+"Half price for certain dishes ("+secondSelectPro+")，saving "+halfPrice+" yuan\n"+"-----------------------------------\n";
            total = (int)secondPrice;
        }else {
            selectItem = firstSelectItem;
            selectPro = "";
            total = (int)firstPrice;
        }
        String output = "============= Order details =============\n" +
                selectItem+
                "-----------------------------------\n" +
                selectPro +
                "Total："+total+" yuan\n" +
                "===================================";


        //System.out.println(output);
        return output;
    }
    class SelectNum{
        private List<String> items;

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

        public int getNum(String id) {
            for(String item : this.getItems()){
                String[] inputSp = item.split("x");
                String inputId = inputSp[0].trim();
                int num = Integer.valueOf(inputSp[1].trim()).intValue();
                if(id.equals(inputId)) {
                    return num;
                }
            }
            return 0;
        }
    }
}
