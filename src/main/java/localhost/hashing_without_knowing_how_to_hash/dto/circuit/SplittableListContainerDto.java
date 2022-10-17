package localhost.hashing_without_knowing_how_to_hash.dto.circuit;

import java.util.ArrayList;
import java.util.List;

/**
 * Can split any given input list with
 * @param <T> object type of list
 * right in the middle into exactly two parts.
 *
 * Is used by CircuitBuilder to recursively build a boolean circuit from formulas
 */
public class SplittableListContainerDto<T> {

    private List<T> wholeList;
    private int listSize;
    private int median;

    public SplittableListContainerDto(List<T> wholeList){
        if(wholeList==null){
            this.wholeList=new ArrayList<>();
        }
        else {
            this.wholeList = wholeList;
        }
        listSize=getListSize();
        median=getMedian();
    }

    /**
     * Let [A,B,C,D] a given list
     * @return [A,B]
     */
    public List<T> leftHalfOfList(){
        List<T> leftHalf=new ArrayList<>();
        for (int i = 0; i < median; i++) {
            leftHalf.add(wholeList.get(i));
        }
        return leftHalf;
    }

    /**
     * Let [A,B,C,D] a given list
     * @return [C,D]
     */
    public List<T> rightHalfOfList(){
        List<T> rightHalf=new ArrayList<>();
        for (int i = median; i < listSize; i++) {
            rightHalf.add(wholeList.get(i));
        }
        return rightHalf;
    }

    private int getMedian(){
        int listSize = getListSize();
        int median = (int) Math.ceil(listSize / 2.0);
        return median;
    }

    private int getListSize(){
        return wholeList.size();
    }


}
