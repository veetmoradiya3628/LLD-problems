import java.util.*;

public class Main {
    class Solution {
        public boolean isPossible(int[] nums) {
            Map<Integer, Integer> mpp = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                mpp.put(nums[i], mpp.getOrDefault(nums[i], 0) + 1);
            }
            int cnt = 0;
            for (Map.Entry<Integer, Integer> entry: mpp.entrySet()){
                if (entry.getValue() >= 2){
                    cnt++;
                }
            }
            return cnt >= 3;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {

        }
        sc.close();
    }
}
