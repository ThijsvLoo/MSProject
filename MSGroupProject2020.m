%% MS Group Project 2020 - Data Analysis
%% Means
% initialise arrays to hold sample means
meansCons = zeros([1,500]);
meansCorp = zeros([1,500]);

% calculate consumer sample means
for k=1:size(ConsumerData,1)
    meansCons(k) = nanmean(ConsumerData(k,:));
end

% calculate corporate sample means
for k=1:size(CorporateData,1)
    meansCorp(k) = nanmean(CorporateData(k,:));
end

% Mean of the consumer means
XbarCons = mean(meansCons);


% Mean of the corporate means
XbarCorp = mean(meansCorp);



%% Performance Bounds
% initialse arrays to hold the service times for consumers
STimeCons90 = zeros([1,500]);
STimeCons95 = zeros([1,500]);
% initialse arrays to hold the service times for corporates
STimeCorp95 = zeros([1,500]);
STimeCorp99 = zeros([1,500]);

% calculate service performance for consumer
for k=1:size(ConsumerData,1)
    % performance bounds
    perf1 = 0.90;
    perf2 = 0.95;
    % get each sample
    sample1 = ConsumerData(k,:);
    % remove NaN elements
    sample1 = rmmissing(sample1);
    % sort sample in ascending order
    s_sample = sort(sample1);
    
    % calculate performance bound indices
    x1 = size(s_sample,2)*perf1;
    x1 = ceil(x1);
    x2 = size(s_sample,2)*perf2;
    x2 = ceil(x2);
    
    % store performance bound element
    STimeCons90(k) = s_sample(x1);
    STimeCons95(k) = s_sample(x2);
end
% calculate mean of x% service time
mean_STimeCons90 = mean(STimeCons90);
mean_STimeCons95 = mean(STimeCons95);

% Boxplot both performance bounds for consumers
figure(1);
boxplot([STimeCons90',STimeCons95'],'Labels',{'90%','95%'})
title("Consumer X% service time");


% calulcate service performance for corporate
for k=1:size(CorporateData,1)
    % performance bounds
    perf1 = 0.95;
    perf2 = 0.99;
    
    % calculate performance bounds for each sample
    sample2 = CorporateData(k,:);
    % get each sample
    sample2 = rmmissing(sample2);
    % sort sample in ascending order
    s_sample2 = sort(sample2);
    
    % calculate performance bound indices
    x1 = size(s_sample2,2)*perf1;
    x1 = ceil(x1);
    x2 = size(s_sample2,2)*perf2;
    x2 = ceil(x2);
    
    % store performance bound element
    STimeCorp95(k) = s_sample2(x1);
    STimeCorp99(k) = s_sample2(x2);
end

% Boxplot both performance bounds for corporate
mean_STimeCorp95 = mean(STimeCorp95);
mean_STimeCorp99 = mean(STimeCorp99);

figure(2);
boxplot([STimeCorp95',STimeCorp99'],'Labels',{'95%','99%'})
title("Corporate X% service time");

%% Perfomance Bounds Confidence Interval

% define alpha
alpha = 0.05;
alpha_s = 1-alpha/2;

% Consumer 90% 
% calculate variance from data
SsquaredCons90 = 0;
for k = 1:size(STimeCons90,2)
    SsquaredCons90 = SsquaredCons90 + (STimeCons90(k) - mean_STimeCons90)^2;
end 
SsquaredCons90 = SsquaredCons90/(size(STimeCons90,2)-1);

% calulcate confidence interval
ConfCons90(1,1) = mean_STimeCons90 - tinv(alpha_s,(size(STimeCons90,2)-1))*sqrt(SsquaredCons90/(size(STimeCons90,2)));
ConfCons90(1,2) = mean_STimeCons90 + tinv(alpha_s,(size(STimeCons90,2)-1))*sqrt(SsquaredCons90/(size(STimeCons90,2)));

% Consumer 95%
% calculate variance from data
SsquaredCons95 = 0;
for k = 1:size(STimeCons95,2)
    SsquaredCons95 = SsquaredCons95 + (STimeCons95(k) - mean_STimeCons95)^2;
end 
SsquaredCons95 = SsquaredCons95/(size(STimeCons95,2)-1);

% calculate confidence interval
ConfCons95(1,1) = mean_STimeCons95 - tinv(alpha_s,(size(STimeCons95,2)-1))*sqrt(SsquaredCons95/(size(STimeCons95,2)));
ConfCons95(1,2) = mean_STimeCons95 + tinv(alpha_s,(size(STimeCons95,2)-1))*sqrt(SsquaredCons95/(size(STimeCons95,2)));


% Corporate 95%
% calculate variance from data
SsquaredCorp95 = 0;
for k = 1:size(STimeCons90,2)
    SsquaredCorp95 = SsquaredCorp95 + (STimeCorp95(k) - mean_STimeCorp95)^2;
end 
SsquaredCorp95 = SsquaredCorp95/(size(STimeCorp95,2)-1);

% calculate confidence interval
ConfCorp95(1,1) = mean_STimeCorp95 - tinv(alpha_s,(size(STimeCorp95,2)-1))*sqrt(SsquaredCorp95/(size(STimeCorp95,2)));
ConfCorp95(1,2) = mean_STimeCorp95 + tinv(alpha_s,(size(STimeCorp95,2)-1))*sqrt(SsquaredCorp95/(size(STimeCorp95,2)));

% Corporate 99%
% calculate variance from data
SsquaredCorp99 = 0;
for k = 1:size(STimeCons90,2)
    SsquaredCorp99 = SsquaredCorp99 + (STimeCorp99(k) - mean_STimeCorp99)^2;
end 
SsquaredCorp99 = SsquaredCorp99/(size(STimeCorp99,2)-1);

% calculate confidence interval
ConfCorp99(1,1) = mean_STimeCorp99 - tinv(alpha_s,(size(STimeCorp99,2)-1))*sqrt(SsquaredCorp99/(size(STimeCorp99,2)));
ConfCorp99(1,2) = mean_STimeCorp99 + tinv(alpha_s,(size(STimeCorp99,2)-1))*sqrt(SsquaredCorp99/(size(STimeCorp99,2)));

%% Average Wait Time Confidence Interval

% Consumer Wait Time 
% calculate variance from data
SsquaredConsT = 0;
for k = 1:size(meansCons,2)
    SsquaredConsT = SsquaredConsT + (meansCons(k) - XbarCons)^2;
end 
SsquaredConsT = SsquaredConsT/(size(meansCons,2)-1);

% calculate confidence interval
ConfConsT(1,1) = XbarCons - tinv(alpha_s,(size(meansCons,2)-1))*sqrt(SsquaredConsT/(size(meansCons,2)));
ConfConsT(1,2) = XbarCons + tinv(alpha_s,(size(meansCons,2)-1))*sqrt(SsquaredConsT/(size(meansCons,2)));

% Corporate Wait Time
% calculate variance from data
SsquaredCorpT = 0;
for k = 1:size(meansCorp,2)
    SsquaredCorpT = SsquaredCorpT + (meansCorp(k) - XbarCorp)^2;
end 
SsquaredCorpT = SsquaredCorpT/(size(meansCorp,2)-1);

% calculate confidence interval
ConfCorpT(1,1) = XbarCorp - tinv(alpha_s,(size(meansCorp,2)-1))*sqrt(SsquaredCorpT/(size(meansCorp,2)));
ConfCorpT(1,2) = XbarCorp + tinv(alpha_s,(size(meansCorp,2)-1))*sqrt(SsquaredCorpT/(size(meansCorp,2)));




