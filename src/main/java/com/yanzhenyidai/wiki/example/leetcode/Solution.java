package com.yanzhenyidai.wiki.example.leetcode;

/**
 * @author frank
 * @version 1.0
 * @date 2020-08-17 16:59
 */
public class Solution {

    public static void main(String[] args) {

        int nums[] = {7, 2, 1};

        int target = 9;


        for (int i = 0; i <= nums.length - 1; i++) {

            for (int j = i + 1; j <= nums.length - 1; j++) {
                if (nums[i] + nums[j] == target) {
//                    return new int[]{i, j};
                    System.out.println(i + "," + j);
                }
//                System.out.println(nums[i] + "," + nums[j]);
            }
        }

    }

    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length - 1; i++) {

            for (int j = 0; j < nums.length - i - 1; j++) {

                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }

        return null;
    }
}
