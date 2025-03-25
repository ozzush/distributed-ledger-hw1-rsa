# RSA Algorithm

Before running the following commands, run

```bash
./gradlew installDist
```

## Keygen

You can generate RSA keys for either data encryption or digital signatures.
The difference is that in the case of data encryption, the encryption key will be short and is meant to be made public,
while the decryption key is long and is meant to be kept private. In the case of digital signatures, 
the encryption key is long and private, and the decryption key is short and public.

### Help
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa keygen -h
```

### Generate encryption keys
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa keygen --codeword <any-string>
```

### Generate digital signature keys
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa keygen --codeword <any-string> --purpose signature
```

## Encryption

### Help
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa encrypt -h
```

### Encrypt text
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa encrypt --key-path=rsa_key.enc --input-text=<text-to-encrypt> --output-file=encrypted-msg.bin
```

### Encrypt file and specify size of the blocks to split it into
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa encrypt --key-path=rsa_key.enc --input-file=<file-to-encrypt> --output-file=encrypted-file.bin --block-size=32
```

## Decryption

### Help
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa decrypt -h
```

### Decrypt file and print result to stdout
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa decrypt --key-path=rsa_key.dec --input-file=<file-to-decrypt>
```

### Decrypt file and save it to an output file
```bash
./build/install/distributed-ledger-hw1-rsa/bin/distributed-ledger-hw1-rsa decrypt --key-path=rsa_key.dec --input-file=<file-to-decrypt> --output-file=decrypted-file.txt
```

