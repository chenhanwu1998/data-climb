create table computer_detail
(
    device_id       int auto_increment
        primary key,
    cpu             varchar(255) null,
    cpu_best        varchar(255) null,
    cpu_level       double       null,
    cpu_desc        varchar(255) null,
    ram             varchar(255) null,
    ram_best        varchar(255) null,
    ram_level       double       null,
    ram_desc        varchar(255) null,
    screen          varchar(255) null,
    screen_best     varchar(255) null,
    screen_level    double       null,
    screen_desc     varchar(255) null,
    res_ratio       varchar(255) null,
    res_ratio_best  varchar(255) null,
    res_ratio_level double       null,
    res_ratio_desc  varchar(255) null,
    weight          varchar(255) null,
    weight_level    double       null,
    weight_best     varchar(255) null,
    weight_desc     varchar(255) null
)
    charset = utf8;

create table computer_param
(
    device_id       int          not null
        primary key,
    price           double       null,
    proceted_time   varchar(255) null,
    disk_capacity   varchar(255) null,
    cpu_serial      varchar(255) null,
    cpu_freq        varchar(255) null,
    cpu_type        varchar(255) null,
    core_size       varchar(255) null,
    three_cache     varchar(255) null,
    rom             varchar(255) null,
    largest_rom     varchar(255) null,
    weight          varchar(255) null,
    product_loc     varchar(255) null,
    os_system       varchar(255) null,
    product_type    varchar(255) null,
    nvidia_type     varchar(255) null,
    nvidia_core     varchar(255) null,
    nvidia_rom_type varchar(255) null,
    nvidia_rom      varchar(255) null,
    other           text         null,
    appendix        varchar(255) null,
    media_device    varchar(255) null,
    cpu             varchar(255) null,
    screen          text         null,
    storage         text         null,
    battery         text         null,
    network         text         null,
    appearance      text         null,
    basic_param     text         null,
    nvidia          text         null,
    io_interface    text         null,
    protected_info  text         null
);

create table device_basic_info
(
    id          int auto_increment
        primary key,
    device_type varchar(255) null,
    pro_id      int          null,
    detail_url  varchar(255) null,
    remark      varchar(255) null,
    name        varchar(255) null,
    img_url     varchar(255) null,
    price       double       null,
    score       double       null,
    heat        bigint       null,
    comment_url varchar(255) null
);

create table device_comment
(
    id          int auto_increment
        primary key,
    device_id   int          null,
    username    varchar(255) null,
    create_date datetime     null,
    article     text         null
);

create table device_company_type
(
    id             int auto_increment
        primary key,
    pro_id         int          null,
    device_type    varchar(255) null,
    cn_name        varchar(255) null,
    name           varchar(255) null,
    en_name        varchar(255) null,
    is_famous      varchar(255) null,
    pro_num        int          null,
    wap_sequence   int          null,
    subcate_id     int          null,
    wx_cnt         varchar(255) null,
    star_user_id   varchar(255) null,
    all_pro_num    int          null,
    sale_level     int          null,
    logo           varchar(255) null,
    av_num         int          null,
    article_num    int          null,
    pic_file_type  varchar(255) null,
    pro_price_num  int          null,
    review_num     int          null,
    bms_sale_level int          null,
    sequence       int          null,
    trademark_pic  varchar(255) null,
    first_word     varchar(255) null
);

create table device_detail
(
    device_id       int          not null
        primary key,
    cpu             varchar(255) null,
    cpu_best        varchar(255) null,
    cpu_level       double       null,
    cpu_desc        varchar(255) null,
    front_pix       varchar(255) null,
    front_pix_best  varchar(255) null,
    front_pix_level double       null,
    front_pix_desc  varchar(255) null,
    back_pix        varchar(255) null,
    back_pix_level  double       null,
    back_pix_desc   varchar(255) null,
    back_pix_best   varchar(255) null,
    ram             varchar(255) null,
    ram_best        varchar(255) null,
    ram_level       double       null,
    ram_desc        varchar(255) null,
    battery         varchar(255) null,
    battery_best    varchar(255) null,
    battery_level   double       null,
    battery_desc    varchar(255) null,
    screen          varchar(255) null,
    screen_best     varchar(255) null,
    screen_level    double       null,
    screen_desc     varchar(255) null,
    res_ratio       varchar(255) null,
    res_ratio_best  varchar(255) null,
    res_ratio_level double       null,
    res_ratio_desc  varchar(255) null
);

create table device_param
(
    device_id      int          not null
        primary key,
    publish_time   varchar(255) null,
    phone_type     varchar(255) null,
    system_kernel  varchar(255) null,
    rom            varchar(255) null,
    ram            varchar(255) null,
    price          varchar(255) null,
    cpu_freq       varchar(255) null,
    proceted_time  text         null,
    camera_num     text         null,
    market_time    text         null,
    cpu_core_num   text         null,
    basic_param    text         null,
    hardware       text         null,
    screen         text         null,
    battery        text         null,
    network        text         null,
    appearance     text         null,
    camera         text         null,
    service        text         null,
    protected_info text         null
);

create table device_performance
(
    device_id   int          not null
        primary key,
    endurance   double       null,
    cost_perf   double       null,
    photo       double       null,
    performance double       null,
    good_word   varchar(255) null,
    total_score double       null
);

create table lhc_data
(
    id      int auto_increment
        primary key,
    year    int          null,
    `index` int          null,
    num     varchar(255) null,
    color   varchar(255) null,
    sx      varchar(255) null
);

create table lhc_data_color
(
    id         int auto_increment
        primary key,
    year_num   int          null,
    index_num  int          null,
    color1     varchar(255) null,
    color2     varchar(255) null,
    color3     varchar(255) null,
    color4     varchar(255) null,
    color5     varchar(255) null,
    color6     varchar(255) null,
    spec_color varchar(255) null
);

create table lhc_data_num
(
    id        int auto_increment
        primary key,
    year_num  int null,
    index_num int null,
    num1      int null,
    num2      int null,
    num3      int null,
    num4      int null,
    num5      int null,
    num6      int null,
    spec_num  int null
);

create table lhc_data_sx
(
    id        int auto_increment
        primary key,
    year_num  int          null,
    index_num int          null,
    sx1       varchar(255) null,
    sx2       varchar(255) null,
    sx3       varchar(255) null,
    sx4       varchar(255) null,
    sx5       varchar(255) null,
    sx6       varchar(255) null,
    spec_sx   varchar(255) null
);

