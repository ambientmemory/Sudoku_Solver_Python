#!/usr/bin/env python
import numpy as np
   
def sudoku_solve():
    global solved
    global curr_no_of_cells_filled
    global current_solution
    global mins
    global count_of_options
    global list_of_options
    if solved == 1: 
        return 0
    if option_calculator() == 0:
        local_list_of_options = list_of_options
        local_count_of_options = count_of_options
        for i in range(0, local_count_of_options):
            current_solution[mins[0]][mins[1]] = local_list_of_options[i]
            curr_no_of_cells_filled += 1
            if curr_no_of_cells_filled == 81:
                solved = 1
                return 0
            local_mins = mins
            print "Debug: Pre recursion curr_cells_filled = ", curr_no_of_cells_filled, ", mins = ", mins, "\n", current_solution
            print "Debug: local_list_of_options[i] = ", local_list_of_options[i], ", local_count_of_options = ", local_count_of_options, ", i = ", i
            if sudoku_solve() == -1:
                curr_no_of_cells_filled -= 1 
                current_solution[local_mins[0]][local_mins[1]] = 0
                mins = local_mins
                print "Debug: Error found at curr_cells_filled = ", curr_no_of_cells_filled, ", mins = ", mins, "\n", current_solution
                print "Debug: local_list_of_options[i] = ", local_list_of_options[i], ", local_count_of_options = ", local_count_of_options, ", i = ", i
                #continue
            else: return 0
    return -1

def option_calculator():
    global current_solution
    global count_of_options
    global list_of_options
    global mins
    count_array = np.full((9,9),10)
    for i in range (9):
        for j in range(9):
            if current_solution[i][j] > 0:
                continue
            count_array[i][j] = 0
            for x in range (1,10):
                if ((check_row(x,i) == 1) and (check_col(x,j) == 1) and (check_square(x,i,j) == 1)):
                    count_array[i][j] += 1
            if count_array[i][j] == 0:
                return -1
    mins = np.unravel_index(count_array.argmin(),count_array.shape)
    index = 0
    for x in range (1,10):
        if ((check_row(x, mins[0])== 1) and (check_col(x, mins[1]) == 1) and (check_square(x, mins[0], mins[1]) == 1)):
            list_of_options[index] = x
            index += 1
    count_of_options = index+1
    return 0
    
def check_row(x, row):
    global current_solution
    for i in range (9):
        if current_solution[row][i] == x:
            return 0
    return 1
    
def check_col(x, col):
    global current_solution
    for i in range (9):
        if current_solution[i][col] == x:
            return 0
    return 1

def check_square(x, row, col):
    global current_solution
    row_shift = (row/3)*3
    col_shift = (col/3)*3
    for p in range (3):
        for q in range (3):
            if current_solution[row_shift+p][col_shift+q] == x:
                return 0
    return 1

solved = 0 
current_solution = np.zeros(81).reshape(9,9)
list_of_options = np.zeros(9)
count_of_options = 10
mins = np.full((1,2),10)
current_solution = np.loadtxt('puzzle3.csv', delimiter = ',')
curr_no_of_cells_filled = np.count_nonzero(current_solution)
print "The problem is: \n",current_solution
    
if sudoku_solve() == -1 :
    print "The problem has no solution..."
else:
    print "The solution is \n",current_solution
