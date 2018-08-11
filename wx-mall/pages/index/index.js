const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../services/user.js');

//获取应用实例
const app = getApp()
Page({
  data: {
    newGoods: [],
    hotGoods: [],
    topics: [],
    brands: [],
    floorGoods: [],
    banner: [],
    channel: [],
    number: 1,
    goodsId: '',
    productId: '',
    goodsInfo: {
      name:'',
      retail_price: '',
      list_pic_url: '',
    },
    showPop: false,
    animationData: {}
  },
  onShareAppMessage: function () {
    return {
      title: 'wgxShop',
      desc: '微果鲜购新鲜商城',
      path: '/pages/index/index'
    }
  },onPullDownRefresh(){
	  	// 增加下拉刷新数据的功能
	    var self = this;
	    this.getIndexData();
 },
  getIndexData: function () {
    let that = this;
    var data = new Object();
    
    util.request(api.IndexUrlHotGoods).then(function (res) {
      if (res.errno === 0) {
        data.hotGoods = res.data.hotGoodsList
      that.setData(data);
      }
    });
    
    util.request(api.IndexUrlBanner).then(function (res) {

      if (res.errno === 0) {
        data.banner = res.data.banner
      that.setData(data);
      }
    });

  },
  // 显示底部弹层
  showModal: function (e) {
    var _this = this;
    var animation = wx.createAnimation({
      duration: 500,
      timingFunction: 'ease',
      delay: 0
    })
    _this.animation = animation
    animation.translateY(300).step()
    _this.setData({
      animationData: animation.export(),
      showPop: true
    })
    setTimeout(function () {
      animation.translateY(0).step()
      _this.setData({
        animationData: animation.export()
      })
    }.bind(_this), 50)


    _this.data.goodsId = e.currentTarget.dataset.id;
  
    // 获取产品ID
    util.request(api.GoodsDetail, { id: _this.data.goodsId }).then(function (res) {
      if (res.errno === 0) {
        _this.data.productId = res.data.info.primary_product_id;
        _this.setData({
          goodsInfo: {
            name: res.data.info.name,
            retail_price: res.data.info.retail_price,
            list_pic_url: res.data.info.list_pic_url,
          },
        });
      }
    });
   
  },
  // 隐藏底部弹层
  hideModal: function () {
    var _this = this;
    // 隐藏遮罩层
    var animation = wx.createAnimation({
      duration: 500,
      timingFunction: "ease",
      delay: 0
    })
    _this.animation = animation
    animation.translateY(300).step()
    _this.setData({
      animationData: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      _this.setData({
        animationData: animation.export(),
        showPop: false
      })
    }.bind(this), 200)
  },


  /**
   * 直接购买
   */
  buyGoods: function () {
    var that = this;
    that.hideModal();
      // 直接购买商品
    util.request(api.BuyAdd, { goodsId: that.data.goodsId, number: that.data.number, productId: that.data.productId}, "POST")
        .then(function (res) {
          let _res = res;
          if (_res.errno == 0) {
            wx.navigateTo({
              url: '/pages/shopping/checkout/checkout?isBuy=true',
            })
          } else {
            wx.showToast({
              image: '/static/images/icon_error.png',
              title: _res.errmsg,
              mask: true
            });
          }

        });
  },


  /**
     * 添加到购物车
     */
  addToCart: function () {
    var that = this;

    //添加到购物车
    util.request(api.CartAdd, { goodsId: that.data.goodsId, number: that.data.number, productId: that.data.productId }, "POST")
        .then(function (res) {
          let _res = res;
          if (_res.errno == 0) {
            wx.showToast({
              title: '添加成功'
            });
            that.setData({
              openAttr: that.data.openAttr,
              cartGoodsCount: _res.data.cartTotal.goodsCount
            });
            if (that.data.userHasCollect == 1) {
              that.setData({
                'collectBackImage': that.data.hasCollectImage
              });
            } else {
              that.setData({
                'collectBackImage': that.data.noCollectImage
              });
            }
          } else {
            wx.showToast({
              image: '/static/images/icon_error.png',
              title: _res.errmsg,
              mask: true
            });
          }

        });

    that.hideModal();
  },
  cutNumber: function () {
    this.setData({
      number: (this.data.number - 1 > 1) ? this.data.number - 1 : 1
    });
  },
  addNumber: function () {
    this.setData({
      number: this.data.number + 1
    });
  },

  onLoad: function (options) {
    this.getIndexData();
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
})
