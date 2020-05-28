%% MS Group Project 2020
%% Mean

meansCons = zeros([1,500]);
meansCorp = zeros([1,500]);

for k=1:size(ConsumerData,1)
    meansCons(k) = nanmean(ConsumerData(k,:));
end

for k=1:size(CorporateData,1)
    meansCorp(k) = nanmean(CorporateData(k,:));
end

XbarCons = mean(meansCons);

XbarCorp = mean(meansCorp);

%% Service requirements
STimeCons1 = zeros([1,500]);
STimeCons2 = zeros([1,500]);

STimeCorp1 = zeros([1,500]);
STimeCorp2 = zeros([1,500]);

% calulcate service performance for consumer
for k=1:size(ConsumerData,1)
    % performance bounds
    perf1 = 0.90;
    perf2 = 0.95;
    % calculate performance bounds for each sample
    sample1 = ConsumerData(k,:);
    sample1 = rmmissing(sample1);
    
    s_sample = sort(sample1);
    
    x1 = size(s_sample,2)*perf1;
    x1 = floor(x1);
    
    x2 = size(s_sample,2)*perf2;
    x2 = floor(x2);
    
    STimeCons1(k) = s_sample(x1);
    STimeCons2(k) = s_sample(x2);
end

mean_STimeCons1 = mean(STimeCons1);
mean_STimeCons2 = mean(STimeCons2);


% calulcate service performance for corporate
for k=1:size(CorporateData,1)
    % performance bounds
    perf1 = 0.95;
    perf2 = 0.99;
    
    % calculate performance bounds for each sample
    sample2 = CorporateData(k,:);
    sample2 = rmmissing(sample2);
    
    s_sample2 = sort(sample2);
    
    x1 = size(s_sample2,2)*perf1;
    x1 = floor(x1);
    
    x2 = size(s_sample2,2)*perf2;
    x2 = floor(x2);
    
    STimeCorp1(k) = s_sample2(x1);
    STimeCorp2(k) = s_sample2(x2);
end

mean_STimeCorp1 = mean(STimeCorp1);
mean_STimeCorp2 = mean(STimeCorp2);

