# MOSI Algorithm: An Efficient Approach for Generalized Power Sum Computation in Arbitrary Arithmetic Sequences
@()[Power Sum Computation|Bernoulli Numbers|Regression Line|Arithmetic Sequence|Lagrange Interpolation|Fast Fourier Transform FFT]
## 
$$
S_k(n) = \sum_{i=1}^{n} \left( a + (i-1) \cdot d \right)^k
$$
## Abstract
This paper introduces the MOSI algorithm, a novel and efficient method for calculating power sums over arbitrary arithmetic sequences, including those with non-integer and negative common differences. Unlike traditional approaches that rely on Bernoulli numbers and are limited to natural number sequences, MOSI generalizes the computation to accommodate any arithmetic sequence. By transforming the power sum problem into a regression problem, the algorithm significantly improves computational efficiency and broadens applicability. MOSI leverages regression analysis, Lagrange interpolation, and FFT to handle various arithmetic sequences effectively, making it suitable for applications in number theory, engineering, and data science.


## Algorithm And Complexity
The MOSI algorithm operates in four key steps:

1. **Generate data points**: Compute the k-th power of the first $k+1$ terms of an arithmetic sequence, obtaining $k+1$ two-dimensional coordinates. For an arithmetic sequence with starting value $a$ and step $d$, the generated points are  $$(a, a^k), (a + d, (a + d)^k)\dots(a + kd, (a + kd)^k)$$
2. **Linear regression**: Calculate the regression line slope and intercept for the first 2 points, first 3 points, and so on, up to the first $k+1$ points, yielding slope array $b$ and intercept array $a$. The slope array $b$ is a polynomial of degree $k-1$ with respect to $x$, and the intercept array $a$ is a polynomial of degree $k$ with respect to $x$. These are solved iteratively using the least squares method. The time complexity is $O(k)$.
   $$\begin{aligned}
   b &\ = \frac{ \sum_{i=1}^{n}  x_i \cdot y_i - n  \cdot \bar x  \cdot \bar y}{  \sum_{i=1}^{n}  x_i^2- n  \cdot \bar x^2} \\
   a& \ =\bar y - b \cdot \bar x
   \end{aligned}
   $$
3. **Lagrange interpolation or FFT**: Obtain the functional forms of the slope array $b$ and the intercept array $a$, denoted by $F_b(x)$ and $F_a(x)$, using Lagrange interpolation or the Fast Fourier Transform (FFT). The final regression line is obtained. The time complexity is $O(k^2)$ or $O(k \log k)$.
   $$
   f(x) \ = \ F_b(n)   \cdot  x  +  F_a(n)
   $$
4. **Constructing the power sum formula**: Using the results from the interpolation, the algorithm reconstructs the general power sum formula for the given arithmetic sequence.
   $$\begin{aligned}
   x_1^k \ + \  x_2^k \  + \  \dots \ + x_n^k & \ = \ f(x_1) + f(x_2) + \ \dots \ + f(x_n)  \\
   & \ = \ [F_b(n)   \cdot x_1  +  F_a(n) ] \\
   & \ + \  [F_b(n)   \cdot  x_2  +  F_a(n) ] \\
   &  \ + \  \dots  \\
   &   \ + \ [F_b(n)   \cdot x_n  +  F_a(n) ] \\
   & \ = \ F_b(n) \cdot (x_1 +x_2+ \dots +x_n) +F_a(n) \cdot N \\
   \end{aligned}$$
   $x_1, x_2 \dots x_n$ is an arithmetic sequence with the first term $x_1$ and a common difference $d$, $N$ is the number of terms in the arithmetic sequence.
   $$\begin{aligned}
   N &= \dfrac{n - x_1}{d} +1\\
   &= \dfrac{n}{d}+(1- \dfrac{x_1}{d})\\
   x_1 +x_2+...+x_n &=(x_1 + n ) \cdot \dfrac{N}{2} \\
   &=\dfrac {n^2}{2 \cdot d} + \dfrac {n}{2}+( \dfrac{x_1}{2} -\dfrac{x_1^2}{2 \cdot d})
   \end{aligned}$$
   Expand it into the form of a general term formula, and thereby obtain the coefficients of each term.
   $$\begin{aligned}
   F_a(n) &= a_k \cdot n^k + a_{k-1} \cdot n^{k-1} + ... + a_0\\
   F_b(n) &= b_{k-1} \cdot n^{k-1} + b_{k-2} \cdot n^{k-2} + ... + b_0\\
   x_1^k \ + \  x_2^k  \ + \  ... \ + x_n^k \ &= \ F_b(n) \cdot (x_1 +x_2+x_3+...+x_n) +F_a(n) \cdot N \\
   &=(b_{k-1} \cdot n^{k-1} + b_{k-2} \cdot n^{k-2} + ... + b_0) \cdot [\dfrac {n^2}{2 \cdot d} + \dfrac {n}{2}+( \dfrac{x_1}{2} -\dfrac{x_1^2}{2 \cdot d})]  \\
   & + ( a_k \cdot n^k + a_{k-1} \cdot n^{k-1} + ... + a_0 ) \cdot [\dfrac{n}{d}+(1- \dfrac{x_1}{d})]
   \end{aligned}$$
#### Time Complexity
- **Step 1 (Generating points)**: This step involves computing the first $k+1$ powers of the sequence terms, resulting in a time complexity of $O(k)$ .
  **Step 2 (Linear regression)**: For each subset of points, the regression line is fitted using the least squares method. The time complexity is $O(k)$, as the number of points and iterations increases linearly with $k$ .
- **Step 3 (Lagrange interpolation or FFT)**: Using FFT to solve the interpolation problem reduces this step’s complexity to $O(k \log k)$ .
-  **Step 4 (Constructing the power sum formula)**: In this step, each term in the summation is multiplied by the corresponding terms in $F_b(n)$, and the two terms in $N$ are multiplied by each term in $F_a(n)$. Given that the number of operations scales linearly with $k$, the overall time complexity for this step is $O(k)$.
- **Overall**: The total time complexity of the algorithm is $O(k \log k)$, significantly improving over traditional methods for large values of  $k$ .

### Space Complexity
- The algorithm requires $O(k)$ space to store the points, slopes, and intercepts, resulting in an overall space complexity of $O(k)$ .


## Example
To calculate the general term coefficients of $1^3 + 2^3 + 3^3 + \dots + n^3$:

1. **Calculate the slope and intercept of the first four points:**
   For points $(1,1^3)$ and $(2,2^3)$, the regression line coefficients are: $a = -6$, $b = 7$
   For points $(1,1^3)$, $(2,2^3)$ and $(3,3^3)$, the regression line coefficients are: $a = -14$, $b = 13$
   For points $(1,1^3)$, $(2,2^3)$, $(3,3^3)$ and $(4,4^3)$, the regression line coefficients are: $a = -27$, $b = 20.8$
   For points $(1,1^3)$, $(2,2^3)$, $(3,3^3)$, $(4,4^3)$ and $(5,5^3)$, the regression line coefficients are: $a = -46.2$, $b = 30.4$
   The following values are obtained after performing the regression analysis.
   | $x$  |   2   |   3  |   4  |   5   |  
   | ---| ----  | ---- | ---- | ----- |
   | $a$  | -6    | -14  |-27   |-46.2  |
   | $b$  | 7     |  13  |20.8  |30.4   |
2. **Derive the slope and intercept functions** using Lagrange interpolation or the Fast Fourier Transform (FFT):
   $$\begin{aligned}
   F_b(n)&=0.9 \cdot n^2 + 1.5 \cdot n  +  0.4 \\
   F_a(n)&= -0.2 \cdot n^3 -0.7 \cdot n^2 -0.7 \cdot n -0.2
   \end{aligned}$$

3. **Substitute into the formula** to calculate the result. This example outlines the steps to compute the general term coefficients using regression and interpolation methods.
   $$\begin{aligned}
   x_1^k \ + \  x_2^k \ + \  x_3^k \ + \  ... \ + x_n^k
   & \ = \ F_b(n) \cdot [\dfrac {n^2}{2 \cdot d} + \dfrac {n}{2}+( \dfrac{x_1}{2} -\dfrac{x_1^2}{2 \cdot d})]  +F_a(n) \cdot  [\dfrac{n}{d}+(1- \dfrac{x_1}{d})] \\
   & \ = \ (0.9 \cdot n^2 + 1.5 \cdot n  +  0.4) \cdot  [\dfrac {n^2}{2 \cdot 1} + \dfrac {n}{2}+( \dfrac{1}{2} -\dfrac{1^2}{2 \cdot 1})] \\
   & \ + \ ( -0.2 \cdot n^3 -0.7 \cdot n^2 -0.7 \cdot n -0.2) \cdot [\dfrac{n}{1}+(1- \dfrac{1}{1})] \\
   & \ = \ (0.9 \cdot n^2 + 1.5 \cdot n  +  0.4) \cdot  [\dfrac {n^2}{2} + \dfrac {n}{2}]  \\
   & \ + \ ( -0.2 \cdot n^3 -0.7 \cdot n^2 -0.7 \cdot n -0.2) \cdot n\\
   & \ = \ (0.9 \cdot \dfrac{1}{2} -0.2) \cdot n^4 + (0.9 \cdot \dfrac{1}{2}+ 1.5 \cdot \dfrac{1}{2} -0.7) \cdot n^3 \\
   & \ + \ (1.5 \cdot \dfrac{1}{2} +0.4 \cdot \dfrac{1}{2}-0.7) \cdot n^2 + (0.4 \cdot \dfrac{1}{2} -0.2) \cdot n\\
   & \ = \ \dfrac{1}{4} \cdot n^4 + \dfrac{1}{2} \cdot n^3 + \dfrac{1}{4} \cdot n^2
   \end{aligned}$$

## Conclusion
The MOSI algorithm provides an efficient and general solution for computing power sums over any arithmetic sequence, surpassing traditional approaches in terms of applicability and computational complexity. By leveraging regression analysis, Lagrange interpolation, and FFT, the algorithm is capable of handling sequences with non-integer and negative steps, making it suitable for a wide range of applications. Future work could explore further optimization techniques or extend the method to other forms of sequences.

## Compare
Performance Comparison between Bernoulli Algorithm and MOSI Algorithm
| **Characteristic**               | **Bernoulli Algorithm**                          | **MOSI Algorithm**                               |
|----------------------------------|--------------------------------------------------|--------------------------------------------------|
| **Applicability**                | Mainly applicable to natural number arithmetic sequences | Applicable to any real-number arithmetic sequence (including non-integer and negative differences) |
| **Time Complexity**              | $ O(k^2)$, optimized to $ O(k \log k) $    | $ O(k^2)$, optimized to $O(k \log k) $    |
| **Space Complexity**             | $O(k) $                                      | $O(k) $                                       |
| **Recursiveness**                | Requires recursive calculation                   | Direct computation without recursion             |
| **Symbolic Computation**         | Primarily supports numerical computation          | Supports both symbolic and numerical computation  |
| **Numerical Stability**          | May suffer from precision issues                 | Improved stability with symbolic computation      |
| **Use Cases**                    | Limited to specific sequences and sum problems    | Broadly applicable to any arithmetic sequence     |


Potential Applications of the MOSI Algorithm
| **Application Area**             | **Potential Uses of MOSI Algorithm**              |
|----------------------------------|--------------------------------------------------|
| **Number Theory and Combinatorics** | Analysis of power sums over arbitrary arithmetic sequences, particularly with symbolic computation |
| **Engineering and Physics**      | Efficient computation of power sums with non-integer or negative differences, useful for modeling systems |
| **Computer Science and Data Analysis** | Applied to analyze large datasets where sequence sums are needed, particularly in generating closed-form solutions |
| **Finance**                      | Fast calculation of power sums in financial models, such as compound interest calculations, producing analytical results |
 