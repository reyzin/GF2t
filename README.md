# Arithmetic operations and polynomial interpolation over GF(2^128) and GF(2^192) in Java

This code implements finite field arithmetic and polynomial interpolation/evaluation in two Galois fields of characteristic 2: GF(2^128) and GF(2^192).
The code is written with performance and side-channel attack resilience in mind.
It is fairly self-explanatory.

Note that for efficiency, polynomial interpolation and evaluation are limited to polynomial inputs that are single bytes
(i.e., at most 256 different inputs). Polynomial values at these inputs can still be arbitrary field
elements.


## Side-channel attack exposure
To reduce the risk of side-channel attacks, the code avoids data-dependent branching and data-dependent table lookups,
with the exception of the faster `mul` function (explained below), which does lookups in (fairly small -- about 512 bytes total) tables.

### You must choose your `mul`
There are two versions provided for each finite field:
one with better performance and one with lower
side-channel risk. (The cost of the lower risk is
a factor of 2-3 in performance of `mul`, which translates into a factor of 1.7-2.5 for everything
 else, except polynomial evaluation, which is not affected, because it uses only special single-byte
 multipliers.) You **must** choose which
version you want by deleting (or renaming) one of the two
redundant `mul` functions in `GF2_128.java` and `GF2_192.java` (else
these files will not compile -- this is by design, to force you to make the choice). See the comments in the code for details.

If you have no secret inputs, choose the faster one. If you do, you have to decide
how bad your side-channel attack exposure is and whether data-dependent indexing into small
tables increases your risk.


## Performance
I have not done thorough head-to-head performance comparisons with other
Java implementations. Here is a rough comparision with C-based [NTL](https://shoup.net/ntl/),
when my code is instantiated with the faster version of `mul`:
my field multiplication is 2-3 times slower;
my field inversion is 1.5-2 times slower; my polynomial interpolation on single-byte polynomial inputs
is also slower (by a factor of 1.1-1.4) than NTL's on full-size inputs.

### More detailed performance numbers

Below are the wall clock timings and comparison with NTL on my machine (MacBookPro, 3.1 GHz Intel Core i5), all in nanonseconds, for GF(2^128):

- Multiplication: 103 (faster version) vs 254 (slower version) vs 45 (NTL)
- Inversion: 1970 (using faster mul) vs 3270 (using slower mul) vs 1381 (NTL)
- Interpolation on 10 byte inputs: 27083 (using faster mul) vs 46738 (using slower mul) vs 24891 (NTL with full-length inputs)
- Evaluation of a degree-9 polynomial on 20 points: 3353 (byte-inputs, mul doesn't matter) vs 11225 (NTL with full-length inputs)

And here are the same for GF(2^192):

- Multiplication: 167 (faster version) vs 520 (slower version) vs 56 (NTL)
- Inversion: 4154 (using faster mul) vs 9114 (using slower mul) vs 2161 (NTL)
- Interpolation on 10 byte inputs: 53670 (using faster mul) vs 126165 (using slower mul) vs 37795 (NTL with full-length inputs)
- Evaluation of a degree-9 polynomial on 20 points: 4090 (byte-inputs, mul doesn't matter) vs 13396 (NTL with full-length inputs)

