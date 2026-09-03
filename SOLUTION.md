My solution for the coding problem uses a regex pattern: 
```
private static final Pattern HREF_PATTERN =
        Pattern.compile("href=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
```

This pattern works by matching on href=" anything here until a "

and then to remove the href part from my results I use:

```
        href.set(matcher.group(1));
```

2 workers 
made using 
```
gcloud dataproc clusters create test-dataproc \
    --project=hidden-expanse-506803-q2 \
    --region=us-east4 \
    --zone=us-east4-a \
    --master-machine-type=e2-standard-2 \
    --worker-machine-type=e2-standard-2 \
    --num-workers=2 \
    --public-ip-address
```
<img width="1780" height="876" alt="image" src="https://github.com/user-attachments/assets/39f9df32-d611-49b7-a4d3-0546b183b794" />


4 workers

made using 
```
gcloud dataproc clusters create test-dataproc \
    --project=hidden-expanse-506803-q2 \
    --region=us-east4 \
    --zone=us-east4-a \
    --master-machine-type=e2-standard-2 \
    --worker-machine-type=e2-standard-2 \
    --num-workers=4 \
    --public-ip-address
```
<img width="1766" height="680" alt="image" src="https://github.com/user-attachments/assets/4e96009e-237b-42e7-829c-39bbaeaaaeb6" />
With 4 workers the execution time basically doubles.


