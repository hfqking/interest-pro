<style scoped>
  .content-background {
    background: #fff;
  }
  .content{
    word-wrap: break-word;
    word-break: break-all;
    overflow: hidden;
  }
  .chat{
    display: inline;
    margin-right: 50px;
    color:#f90;
  }
  .chat span{
    margin-left: 2px;
  }
  .title-text{
    color: #2d64b3;
  }
</style>
<template>
    <div id="mywork">
	    <div class="page-header-main content-background">
	      <div class="box-flex width-80 margin-auto margin-top-2 flex-direction-column flex-justify-center flex-items-center" >
	        <div class="line-height-50 font-size-22">{{insterest.title}}</div>
	        <div class="text-align-center margin-top-2">
	        </div>
            <div style="width: 80%;margin-top: 20px">
              <div class="ql-container">
                  <div class="ql-editor">
                     <div v-html="insterest.content"></div>
                  </div>
              </div>
            </div>
            
            <div style="width: 100%;margin: 10% 0 20px 0">
                <div class="ivu-card-head" style="background: #eceef2">
                    <p>看帖</p>
                </div>
                <Card v-for="(item,index) in postcardList" :key="index">
                    <div slot="title">
                        <div class="chat">
                          <Icon type="md-chatboxes" size="25" />
                          <span>{{item.replyCount}}</span>
                        </div>
                        <router-link :to="('/bbs/card/'+item.id)">
                            <span class="tirtleFont lineThrou title-text">{{item.title}}</span>
                        </router-link>

                    </div>
                    <div slot="extra">
                        <a :href="$store.state.userUrlPre+item.userId" target="_blank">
                            <!-- <Icon type="ios-person"></Icon> -->
                            <img :src="item.headImg" style="width: 25px;height: 25px;border-radius: 100%;">
                            {{item.userName}}
                        </a>
                    </div>
                    <div>
                        <p class="content">{{item.content}}</p>
                        <span>
                            <Icon type="ios-time"></Icon>
                            {{item.replyTime}}
                        </span>
                    </div>
                </Card>
                <div style="margin-top: 20px">
                    <Page :total="total" :page-size="pageInfo.pageSize" show-elevator show-total @on-change="e=>{pageSearch(e)}"></Page>
                </div>
            </div>

            <div class="box-flex width-100 margin-auto margin-top-2 border-top border-color-bfbfbf"></div>

            <div class="box-flex margin-auto margin-top-2 flex-direction-column flex-justify-center flex-items-center" style="width: 100%">
                <div class=" width-100 flex-direction-row">
                  <div class="box-flex flex-1 padding-all-5x">
                    <span><Icon type="edit"></Icon>发帖</span>
                  </div>
                  <div class="box-flex flex-6 width-100 padding-all-5x">
                    <Input v-model="title" placeholder="标题" />
                  </div>
                  <div class="box-flex flex-6 width-100 padding-all-5x">
                    <Input v-model="textarea" type="textarea" :rows="6" placeholder="内容" />
                  </div>
                </div>
                <div class="box-flex width-100 margin-top-2 flex-items-flex-end flex-justify-flex-end margin-bottom-3">
                  <Button type="primary" @click="sendCard()">发表</Button>
                </div>
              </div>
	      </div>
	    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      postcardList: [],
      interestid: null,
      insterest: {
        id: "",
        title: "",
        info: "",
        content: "",
        image: ""
      },
      total: 0,
      pageInfo: {
        page: 0,
        pageSize: 20
      },
      textarea: "",
      title: ""
    };
  },
  mounted() {
    this.interestid = this.$route.params.id;
    this.getArticle(this.$route.params.id);
    this.getPostCard({
      pageInfo: this.pageInfo,
      interestid: this.interestid
    });
  },
  methods: {
    getArticle(e) {
      this.axios({
        method: "get",
        url: "/interest/bbs/public/interests/interest",
        params: {
          id: e
        }
      })
        .then(
          function(response) {
            this.insterestSet(response.data.data);
          }.bind(this)
        )
        .catch(
          function(error) {
            alter(error);
          }.bind(this)
        );
    },

    listDateSet(e) {
      for (var i = e.length - 1; i >= 0; i--) {
        e[i].replyTime = this.dateGet(e[i].replyTime);
      }
    },
    getPostCard(e) {
      this.axios({
        method: "get",
        url: "/interest/bbs/public/postcards",
        params: {
          page: e.pageInfo.page,
          pageSize: e.pageInfo.pageSize,
          interestId: e.interestid
        }
      })
        .then(
          function(response) {
            console.log();
            this.postcardList = response.data.data.data;
            this.listDateSet(this.postcardList);
            this.total = response.data.data.totalCount;
          }.bind(this)
        )
        .catch(function(error) {
          alert(error);
        });
    },
    pageSearch(e) {
      this.pageInfo.page = e - 1;
      this.getPostCard({
        pageInfo: this.pageInfo,
        interestid: this.interestid
      });
    },
    insterestSet(e) {
      this.insterest.id = e.id;
      this.insterest.title = e.title;
      this.insterest.info = e.info;
      this.insterest.content = e.content;
      this.insterest.image = e.image;
    },
    sendCard() {
      if (
        this.title != null &&
        this.textarea != null &&
        this.title != "" &&
        this.textarea != ""
      ) {
        if (
          this.axios.defaults.headers.common["Authorization"] != null &&
          this.axios.defaults.headers.common["Authorization"] != ""
        ) {
          this.axios({
            method: "post",
            url: "/interest/bbs/postcards/postcard",
            data: {
              interestId: this.interestid,
              title: this.title,
              content: this.textarea
            }
          })
            .then(
              function(response) {
                this.$Message.info("发帖成功");
                this.title = "";
                this.textarea = "";
                this.pageInfo.page = 0;
                this.getPostCard({
                  pageInfo: this.pageInfo,
                  interestid: this.interestid
                });
              }.bind(this)
            )
            .catch(
              function(error) {
                alter(error);
              }.bind(this)
            );
        } else {
          this.$Message.error("登录后，才能发帖！");
        }
      } else {
        this.$Message.error("请填写标题和内容");
      }
    }
  }
};
</script>
