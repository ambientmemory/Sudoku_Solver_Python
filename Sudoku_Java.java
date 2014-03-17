package sudoku;

import java.io.*;
import java.util.*;
//import static solution.test.debug_print;

/**
 *
 * @author Piyali Mukherjee
 */
public class Sudoku {

	static boolean solved = false;
	static int[][] current_solution = new int[9][9];
	static int curr_x = 10;							//Tracks the latest point of change
	static int curr_y = 10;
	static int orig_no_of_cells_filled;
	static int curr_no_of_cells_filled;
	static int[][] original_prob = new int[9][9];
	/**
	 * This matrix will contain the no of options at each point The 1st position will designate the number of options at each point The rest will contain the numbers 1-9
	 */
	static int[] list_of_options = new int[9];		//well, the cell with min no of options can have max 9 options
	static int count_of_options = 10;		//Actual count of options available at min location
	static int min_i = 10;
	static int min_j = 10;

	static int sudoku_solve() {
		if (solved) {
			return (0); //Problem has been solved.
		}
		/**
		 * Now we will populate our options in options array. Each pre-filled cell will be assigned -1, so that we can find 1 as the minimum number of options *
		 */
		//The following track the properties of the location with min number of options. All this madness is because Java will not pass by reference

		int[] local_list_of_options = new int[9];
		int local_count_of_options;
		int local_min_i;
		int local_min_j;

		if (option_calculator() == 0) {
			/*
			 * This is an unusually good news. We have options, the location of minimum option is captured in min_i & min)j, and the count_of_options is properly updated.
			 * We systematically, try each one, and then call the function again. We of course, increase the global count of total cells filled, and the latest cell to be filled
			 */

			//We first copy the global values to the local array - this is because Java does not pass be reference, it ONLY passes by value	
			local_count_of_options = count_of_options;
			local_min_i = min_i;
			local_min_j = min_j;
			for (int i = 0; i < local_count_of_options; i++) {
				local_list_of_options[i] = list_of_options[i];
			}

			for (int i = 0; i < local_count_of_options; i++) {
				current_solution[local_min_i][local_min_j] = local_list_of_options[i];
				if (++curr_no_of_cells_filled == 81) {
					solved = true;
					return(0);
				}
				curr_x = local_min_i;
				curr_y = local_min_j;
				debug_print();
				if (sudoku_solve() == -1) {
					curr_no_of_cells_filled--;
					current_solution[local_min_i][local_min_j] = 0;		//undo the cell population
					continue;
				} else {
					if (curr_no_of_cells_filled == 81) {
						solved = true;
					}
					return (0);
				}
			}
		} else {	//no options left
			if (curr_no_of_cells_filled == 81) {
				solved = true;
				return (0);
			} else {
				return (-1);
			}
		}
		return (-1);
	}

	static int option_calculator() {
		int[][] count = new int[9][9];
		int min_option_count = 10;
		int curr_min_i = 10;
		int curr_min_j = 10;
		int[] options = new int[9];
		//Determines the options, populates the option count of options and the options matrix, and while at it, determines the minimum option condition in the same loop.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (current_solution[i][j] > 0) {
					count[i][j] = 0;
					continue;
				} //Piusona, if you write an if() such that it continues, or returns, then please dont write else - such an immature thing to do !!!
				for (int x = 1; x <= 9; x++) {//running through all possible suspects
					if (check_row(x, i) && check_col(x, j) && check_square(x, i, j)) {
						count[i][j]++;
					}
				} //end of for loop - all numbers tried
				if (count[i][j] == 0) {
					//We could not find any solution for the current [i][j], this needs to return, as there exists an [i][j] that is not allottable
					return (-1);
				}
				if (count[i][j] < min_option_count) {
					min_option_count = count[i][j];
					curr_min_i = i;
					curr_min_j = j;
				}
			}
		} //Completed building our options count list
		//Now we build the options list corresponding to the minimum count
		int	index = 0;
		for (int x = 1; x <= 9; x++) {//running through all possible suspects
			if (check_row(x, curr_min_i) && check_col(x, curr_min_j) && check_square(x, curr_min_i, curr_min_j)) {
				options[index++] = x;
			}
		} //end of for loop - all numbers tried

		//Now we pass the values back using the static variables.
		count_of_options = min_option_count;		//Actual count of options available at min location
		min_i = curr_min_i;
		min_j = curr_min_j;
		for (int i = 0; i < count_of_options; i++) {
			list_of_options[i] = options[i];
		}
		return (0);
	}

	static boolean check_row(int x, int row) {
		boolean result = true;
		for (int index = 0; index < 9; index++) {
			if (current_solution[row][index] == x) {
				result = false;
				break;
			}
		}
		return result;
	}

	static boolean check_col(int x, int col) {
		boolean result = true;
		for (int index = 0; index < 9; index++) {
			if (current_solution[index][col] == x) {
				result = false;
				break;
			}
		}
		return result;
	}

	static boolean check_square(int x, int row, int col) {
		boolean result = true;
		int row_shift = (row / 3) * 3;
		int col_shift = (col / 3) * 3;
		for (int p = 0; p < 3; p++) {
			for (int q = 0; q < 3; q++) {
				if (current_solution[row_shift + p][col_shift + q] == x) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	static void debug_print() {
		System.out.println("The state after filling up "+curr_no_of_cells_filled+" is as below. The last change was at row: "+(curr_x+1)+" and col : "+(curr_y+1));
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(current_solution[i][j] + "\t ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		orig_no_of_cells_filled = 0;
		curr_no_of_cells_filled = 0;

		original_prob[0][3] = 2;
		original_prob[0][7] = 6;
		original_prob[0][8] = 3;
		original_prob[1][0] = 3;
		original_prob[1][5] = 5;
		original_prob[1][6] = 4;
		original_prob[1][8] = 1;
		original_prob[2][2] = 1;
		original_prob[2][5] = 3;
		original_prob[2][6] = 9;
		original_prob[2][7] = 8;
		original_prob[3][7] = 9;
		original_prob[4][3] = 5;
		original_prob[4][4] = 3;
		original_prob[4][5] = 8;
		original_prob[5][1] = 3;
		original_prob[6][1] = 2;
		original_prob[6][2] = 6;
		original_prob[6][3] = 3;
		original_prob[6][6] = 5;
		original_prob[7][0] = 5;
		original_prob[7][2] = 3;
		original_prob[7][3] = 7;
		original_prob[7][8] = 8;
		original_prob[8][0] = 4;
		original_prob[8][1] = 7;
		original_prob[8][5] = 1;
		orig_no_of_cells_filled = 27;
		curr_no_of_cells_filled = 27;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				// int n = in.nextInt();
				//System.out.print("Enter row "+(i+1)+" col "+(j+1)+" ... ");
				current_solution[i][j] = original_prob[i][j];
			}
		}
		debug_print();
//		int n = in.nextInt();		//Just a wait to check the input

		if (sudoku_solve() == -1) {
			System.out.println("The problem has no solution...");
		} else {
			System.out.println("The solution is :");
			debug_print();
		}
	}
}//End of class Sudoku
