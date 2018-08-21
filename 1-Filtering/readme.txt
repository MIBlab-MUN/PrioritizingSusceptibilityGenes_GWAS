This program receives genome information of samples froma file (inputFile) in the following format
            and returns the result in a file (outputFile) with a similar format including only selected SNPs (instead of all of them).
            - inputFileFormat:
            It is a table where each row represents a sample and the first 6 columns are personal information
            of that sample and the rest of the columns are SNPs, where the column labels reflect the snp name (e.g. snp1) with the name of the minor allele appended (i.e. snp1_2 in the first instance, as 2 is the minor allele) for the additive component.  Assuming A is the minor allele, will recode genotypes as follows:
                SNP       SNP_A
                ---       -----
                A A   ->    0
                A T   ->    1
                T T   ->    2
                0 0   ->   NA
 This code is able to use (ReliefF + TURF) or (SURF + TURF) for filtering.